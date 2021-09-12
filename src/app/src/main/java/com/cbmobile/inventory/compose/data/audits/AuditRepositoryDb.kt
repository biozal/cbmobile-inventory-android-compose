package com.cbmobile.inventory.compose.data.audits

import android.content.Context
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.models.Audit
import com.cbmobile.inventory.compose.models.AuditWrapper
import com.couchbase.lite.MutableDocument
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class AuditRepositoryDb(var context: Context) : AuditRepository {
   private val databaseResources: InventoryDatabase = InventoryDatabase.getInstance(context)

    override suspend fun getAuditsByProjectId(projectId: String): List<Audit> {
        val list = ArrayList<Audit>()
        return withContext(Dispatchers.IO){
            try {
                val db = databaseResources.databases[databaseResources.projectDatabaseName]?.database
                db?.let  { database ->
                    val query = database.createQuery("SELECT * FROM project AS audit WHERE type=\"audit\" AND projectId=\"$projectId\"")
                    query.execute().forEach { result ->
                        val json = result.toJSON()
                        val auditWrapper = Gson().fromJson(json, AuditWrapper::class.java)
                        list.add(auditWrapper.audit)
                    }
                }
            } catch (e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext list
        }
    }

    override suspend fun saveAudit(audit: Audit) {
        return withContext(Dispatchers.IO){
            try {
                val db = databaseResources.databases[databaseResources.projectDatabaseName]?.database
                db?.let { database ->
                    val json = Gson().toJson(audit)
                    val doc = MutableDocument(audit.auditId, json)
                    database.save(doc)
                }
            }catch(e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
        }
    }

    override suspend fun deleteAudit(auditId: String) : Boolean {
        return withContext(Dispatchers.IO) {
            var result = false
            try {
                val db =
                    databaseResources.databases[databaseResources.projectDatabaseName]?.database
                db?.let { database ->
                    val doc = database.getDocument(auditId)
                    doc?.let { document ->
                        db.delete(document)
                        result = true
                    }
                }
            } catch (e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext result
        }
    }
}