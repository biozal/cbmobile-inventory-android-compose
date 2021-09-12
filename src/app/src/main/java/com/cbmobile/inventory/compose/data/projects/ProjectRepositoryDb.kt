package com.cbmobile.inventory.compose.data.projects

import android.content.Context

import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

import com.couchbase.lite.*

import com.cbmobile.inventory.compose.data.DatabaseResource
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.LocationWrapper
import com.cbmobile.inventory.compose.models.Project
import com.cbmobile.inventory.compose.models.ProjectWrapper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectRepositoryDb(var context: Context) : ProjectRepository {

    private val databaseResources: InventoryDatabase = InventoryDatabase.getInstance(context)

    override suspend fun getProject(projectId: String): Project {
        return withContext(Dispatchers.IO){
            try {
                val db = databaseResources.databases[databaseResources.projectDatabaseName]?.database
                val doc = db?.getDocument(projectId)
                if (doc == null){
                    return@withContext Project(projectId, "", "", false, "project", null)
                } else {
                    return@withContext Gson().fromJson(doc.toJSON(), Project::class.java)
                }
            } catch (e: Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext Project(projectId, "", "", false, "project", null)
        }
    }

    override suspend fun saveProject(project: Project) : Boolean {
        var results = false;
        return withContext(Dispatchers.IO) {
           try{
               val db = databaseResources.databases[databaseResources.projectDatabaseName]?.database
               val json = Gson().toJson(project)
               val doc = MutableDocument(project.projectId, json)
               db?.save(doc)
               results = true
           } catch (e: Exception){
               android.util.Log.e(e.message, e.stackTraceToString())
           }
            return@withContext results
        }
    }

    override suspend fun getProjects(): List<Project> {
        var list = ArrayList<Project>()
        return withContext(Dispatchers.IO) {
            try {
                val db =
                    databaseResources.databases[databaseResources.projectDatabaseName]?.database
                val query =
                    db?.createQuery("SELECT * FROM project WHERE type = \"project\"")
                query?.execute()?.forEach {
                    val json = it.toJSON()
                    val projectWrapper = Gson().fromJson(json, ProjectWrapper::class.java)
                    list.add(projectWrapper.project)
                }
            } catch (e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext list
        }
    }

    override suspend fun deleteProject(projectId: String): Boolean {
        var result = false;
        return withContext(Dispatchers.IO){
            try {
                val db =
                    databaseResources.databases[databaseResources.projectDatabaseName]?.database
                val projectDoc = db?.getDocument(projectId)
                if (projectDoc != null && projectDoc.id == projectId){
                    db.delete(projectDoc)
                    result = true
                }
            } catch (e: java.lang.Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext result
        }
    }

    override suspend fun getLocations(): List<Location> {
        return withContext(Dispatchers.IO) {
            var locationResults = ArrayList<Location>()
            try {
                val db =
                    databaseResources.databases[databaseResources.projectDatabaseName]?.database
                val query =
                    db?.createQuery("SELECT * FROM project AS location WHERE type = \"location\"")
                query?.execute()?.forEach {
                    val json = it.toJSON()
                    val locationWrapper = Gson().fromJson(json, LocationWrapper::class.java)
                    locationResults.add(locationWrapper.location)
                }
            } catch (e: Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext locationResults
        }
    }

    override suspend fun initializeDatabase() {
        return withContext(Dispatchers.IO) {
            try {
                val dbConfig = DatabaseConfiguration()
                dbConfig.directory = context.filesDir.toString()

                //if databases don't exist create them from embedded asset
                if (!Database.exists(databaseResources.projectDatabaseName, context.filesDir)) {

                    //get location database zip file from apk, write to disk
                    val locationDbPath = File(context.filesDir.toString())
                    unzip(context.assets.open("locations.zip"), locationDbPath)

                    //copy the location database to the project database
                    val locationDbFile = File(
                        String.format(
                            "%s/%s",
                            context.filesDir,
                            (databaseResources.locationDatabase + ".cblite2")
                        ))
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

                //store pointers for later use
                val databaseResource = DatabaseResource(database, dbConfig)
                databaseResources.databases[databaseResources.projectDatabaseName] =
                    databaseResource

            } catch (e: Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
        }
    }

    private fun unzip(`in`: InputStream, destination: File) {
        val buffer = ByteArray(1024)
        val zis = ZipInputStream(`in`)
        var ze: ZipEntry? = zis.nextEntry
        while (ze != null) {
            val fileName: String = ze.name
            val newFile = File(destination, fileName)
            if (ze.isDirectory) {
                newFile.mkdirs()
            } else {
                File(newFile.parent).mkdirs()
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
        `in`.close()
    }
}