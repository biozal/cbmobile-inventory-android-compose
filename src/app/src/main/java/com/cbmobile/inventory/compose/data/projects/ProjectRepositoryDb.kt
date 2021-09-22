@file:Suppress("SameParameterValue")

package com.cbmobile.inventory.compose.data.projects

import android.content.Context

import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

import com.couchbase.lite.*

import com.cbmobile.inventory.compose.data.DatabaseResource
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.LocationDTO
import com.cbmobile.inventory.compose.models.Project
import com.cbmobile.inventory.compose.models.ProjectDTO
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@InternalCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
class ProjectRepositoryDb(var context: Context) : ProjectRepository {
    private val databaseResources: InventoryDatabase = InventoryDatabase.getInstance(context)

    override suspend fun getProject(projectId: String): Project {
        return withContext(Dispatchers.IO){
            try {
                val db = databaseResources.databases[databaseResources.projectDatabaseName]?.database
                db?.let { database ->
                    val doc = database.getDocument(projectId)
                    doc?.let { document  ->
                        return@withContext Gson().fromJson(document.toJSON(), Project::class.java)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext Project(projectId = projectId, createdOn = Date(), modifiedOn = Date())
        }
    }

    fun getProjectDocumentChangeFlow(documentId: String) : Flow<Project?>?{
        val db =
            databaseResources.databases[databaseResources.projectDatabaseName]?.database
        db?.let { database ->
            return database.documentChangeFlow(documentId)
                .map { dc -> mapDocumentChangeToProject(dc) }
                .flowOn(Dispatchers.IO)
        }
        return null
    }

    private fun mapDocumentChangeToProject(documentChange: DocumentChange) : Project?{
        var project: Project? = null
        val db =
            databaseResources.databases[databaseResources.projectDatabaseName]?.database
        db?.let { database ->
            val doc = database.getDocument(documentChange.documentID)
            doc?.let { document ->
                project = Gson().fromJson(document.toJSON(), Project::class.java)
            }
        }
        return project
    }

    override fun getProjectsFlow(): Flow<List<Project>>? {
        try {
            val db =
                databaseResources.databases[databaseResources.projectDatabaseName]?.database
            db?.let { database ->
                val query = database.createQuery("SELECT * FROM project AS item WHERE type = \"project\"")
                val flow = query
                    .queryChangeFlow()
                    .map{ qc -> mapQueryChangeToProjects(qc)}
                    .flowOn(Dispatchers.IO)
                query.execute()
                return flow
            }
        } catch (e: Exception){
            android.util.Log.e(e.message, e.stackTraceToString())
        }
        return null
    }

    private fun mapQueryChangeToProjects(queryChange: QueryChange) : List<Project>{
        val projects = mutableListOf<Project>()
        queryChange.results?.let { results ->
            results.forEach() { result ->
                projects.add(Gson().fromJson(result.toJSON(), ProjectDTO::class.java).item)
            }
        }
        return projects
    }

    override suspend fun saveProject(project: Project) {
        return withContext(Dispatchers.IO) {
           try{
               val db = databaseResources.databases[databaseResources.projectDatabaseName]?.database
               db?.let { database ->
                   val json = Gson().toJson(project)
                   val doc = MutableDocument(project.projectId, json)
                   database.save(doc)
               }
           } catch (e: Exception){
               android.util.Log.e(e.message, e.stackTraceToString())
           }
        }
    }

    override suspend fun deleteProject(projectId: String) : Boolean {
        return withContext(Dispatchers.IO){
            var result = false
            try {
                val db =
                    databaseResources.databases[databaseResources.projectDatabaseName]?.database
                db?.let { database ->
                    val projectDoc = database.getDocument(projectId)
                    projectDoc?.let { document ->
                        db.delete(document)
                        result = true
                    }
                }
            } catch (e: java.lang.Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext result
        }
    }

    override suspend fun completeProject(projectId: String) {
        return withContext(Dispatchers.IO){
            try{
                val db = databaseResources.databases[databaseResources.projectDatabaseName]?.database
                db?.let { database ->
                    val doc = database.getDocument(projectId)
                    doc?.let { document ->
                        val mutDoc = document.toMutable()
                        mutDoc.setBoolean("complete", true)
                        database.save(mutDoc)
                    }
                }
            } catch(e: java.lang.Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
        }
    }



    override suspend fun initializeDatabase() {
        return withContext(Dispatchers.IO) {
            try {
                val dbConfig = DatabaseConfigurationFactory.create(context.filesDir.toString())

                //if databases don't exist create them from embedded asset
                if (!Database.exists(databaseResources.projectDatabaseName, context.filesDir)) {

                    //get location database zip file from apk, write to disk
                    val locationDbPath = File(context.filesDir.toString())
                    //unzip(context.assets.open("locations.zip"), locationDbPath)
                    unzip("locations.zip", locationDbPath)

                    //copy the location database to the project database
                    val locationDbFile = File( String.format( "%s/%s", context.filesDir, (databaseResources.locationDatabase + ".cblite2") ))
                    Database.copy(locationDbFile, databaseResources.projectDatabaseName, dbConfig)
                }
                //get database and store pointer for later use
                val database = Database(databaseResources.projectDatabaseName, dbConfig)

                //create index for document type if it doesn't exist
                if (!database.indexes.contains("typeIndex")) {
                    database.createIndex(
                        "typeIndex", IndexBuilder.valueIndex(
                            ValueIndexItem.expression(
                                Expression.property("type")
                            )))
                }
                if (!database.indexes.contains("projectIndex")){
                    database.createIndex(
                        "projectIndex", IndexBuilder.valueIndex(
                            ValueIndexItem.expression(
                                Expression.property("projectId"),
                            )))
                }
                //store pointers for later use
                val databaseResource = DatabaseResource(database, dbConfig)
                databaseResources.databases[databaseResources.projectDatabaseName] =
                    databaseResource
            } catch (e: Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
        }
    }

    //private fun unzip(stream: InputStream, destination: File) {
    private fun unzip(
        file: String,
        destination: File
    ) {
        context.assets.open(file).use { stream ->
            val buffer = ByteArray(1024)
            val zis = ZipInputStream(stream)
            var ze: ZipEntry? = zis.nextEntry
            while (ze != null) {
                val fileName: String = ze.name
                val newFile = File(destination, fileName)
                if (ze.isDirectory) {
                    newFile.mkdirs()
                } else {
                    File(newFile.parent!!).mkdirs()
                    val fos = FileOutputStream(newFile)
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                    fos.close()
                }
                ze = zis.nextEntry
            }
            zis.closeEntry()
            zis.close()
            stream.close()
        }
    }
}
