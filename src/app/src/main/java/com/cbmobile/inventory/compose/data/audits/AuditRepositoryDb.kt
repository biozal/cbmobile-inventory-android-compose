package com.cbmobile.inventory.compose.data.audits

import android.content.Context
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.models.Audit
import com.cbmobile.inventory.compose.models.AuditModelDTO
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.QueryChange
import com.couchbase.lite.queryChangeFlow
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class AuditRepositoryDb(var context: Context) : AuditRepository {
   private val databaseResources: InventoryDatabase = InventoryDatabase.getInstance(context)

    override fun getAuditsByProjectId(projectId: String): Flow<List<Audit>>? {
        try {
            val db = databaseResources.databases[databaseResources.projectDatabaseName]?.database
            db?.let  { database ->
                val query = database.createQuery("SELECT * FROM _ AS item WHERE type=\"audit\" AND projectId=\"$projectId\"")
                val flow = query
                    .queryChangeFlow()
                    .map { qc -> mapQueryChangeToAudit(qc)}
                    .flowOn(Dispatchers.IO)
                query.execute()
                return flow
            }
        } catch (e: Exception){
            android.util.Log.e(e.message, e.stackTraceToString())
        }
        return null
    }

    override suspend fun getAudit(projectId: String, auditId: String): Audit {
        return withContext(Dispatchers.IO){
            try {
                val db = databaseResources.databases[databaseResources.projectDatabaseName]?.database
                db?.let { database ->
                    val doc = database.getDocument(auditId)
                    doc?.let { document  ->
                        return@withContext Gson().fromJson(document.toJSON(), Audit::class.java)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext Audit(
                projectId = projectId,
                auditId = UUID.randomUUID().toString(),
                createdOn = Date(),
                modifiedOn =  Date(),
                createdBy = databaseResources.loggedInUser!!.username,
                modifiedBy = databaseResources.loggedInUser!!.password,
                team = databaseResources.loggedInUser!!.team)
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

    private fun mapQueryChangeToAudit (queryChange: QueryChange) : List<Audit> {
        val audits = mutableListOf<Audit>()
        queryChange.results?.let { results ->
            results.forEach(){ result ->
                audits.add(Gson().fromJson(result.toJSON(), AuditModelDTO::class.java).item)
            }
        }
        return audits
    }
}