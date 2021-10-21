package com.cbmobile.inventory.compose.data.audits

import com.cbmobile.inventory.compose.models.Audit
import kotlinx.coroutines.flow.Flow

interface AuditRepository {

    fun getAuditsByProjectId(projectId: String): Flow<List<Audit>>?

    suspend fun getAudit(projectId: String, auditId: String): Audit

    suspend fun saveAudit(audit: Audit)

    suspend fun deleteAudit(auditId: String) : Boolean
}