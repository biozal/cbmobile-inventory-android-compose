package com.cbmobile.inventory.compose.data.audits

import android.content.Context
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.models.Audit
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
                    val query = database.createQuery("")
                }
            } catch (e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext list
        }
    }

    override suspend fun saveAudit(audit: Audit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAudit(auditId: String) {
        TODO("Not yet implemented")
    }
}