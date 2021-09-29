package com.cbmobile.inventory.compose.data

import android.content.Context
import com.cbmobile.inventory.compose.models.UserProfile
import com.cbmobile.inventory.compose.util.Singleton
import com.couchbase.lite.*
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class InventoryDatabase private constructor(val context: Context) {

    var databases: MutableMap<String, DatabaseResource> = mutableMapOf()
    var projectDatabaseName = "project"
    private var locationDatabase = "locations"
    var loggedInUser: UserProfile? = null

    init {
        //setup couchbase lite
        CouchbaseLite.init(context)

        Database.log.console.domains = LogDomain.ALL_DOMAINS
        Database.log.console.level = LogLevel.VERBOSE
    }

    fun getTeamProjectDatabaseName() : String{
        loggedInUser?.team?.let { team ->
            projectDatabaseName = "project$team"
            return "project$team"
        }
        return ""
    }

    fun deleteDatabase() {
        try {
            databases.forEach {
                if (Database.exists(it.key, context.filesDir)){
                    it.value.database.close()
                    Database.delete(it.key, context.filesDir)
                }
            }
            databases.clear()
        } catch (e: Exception){
            android.util.Log.e(e.message, e.stackTraceToString())
        }
    }

    fun closeDatabases() {
        try {
            databases.forEach{
                it.value.replicator?.stop()
                it.value.database.close()
            }
            databases.clear()
            projectDatabaseName = "project"
        } catch (e: java.lang.Exception){
            android.util.Log.e(e.message, e.stackTraceToString())
        }
    }

    fun initializeDatabase() {
        try {
            loggedInUser?.let { _ ->
                val databaseName = getTeamProjectDatabaseName()
                val dbConfig = DatabaseConfigurationFactory.create(context.filesDir.toString())
                //if databases don't exist create them from embedded asset
                if (!Database.exists(databaseName, context.filesDir)) {

                    //get location database zip file from apk, write to disk
                    val locationDbPath = File(context.filesDir.toString())
                    //unzip(context.assets.open("locations.zip"), locationDbPath)
                    unzip("locations.zip", locationDbPath)

                    //copy the location database to the project database
                    val locationDbFile = File( String.format( "%s/%s", context.filesDir, ("${locationDatabase}.cblite2") ))
                    Database.copy(locationDbFile, databaseName, dbConfig)
                }
                //get database and store pointer for later use
                val database = Database(databaseName, dbConfig)

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
                val databaseResource = DatabaseResource(database)
                databases[databaseName] = databaseResource
            }
        } catch (e: Exception) {
            android.util.Log.e(e.message, e.stackTraceToString())
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

    companion object : Singleton<InventoryDatabase, Context>(::InventoryDatabase)
}