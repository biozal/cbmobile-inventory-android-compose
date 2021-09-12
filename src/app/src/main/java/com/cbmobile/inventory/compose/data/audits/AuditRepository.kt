package com.cbmobile.inventory.compose.data.audits

import com.cbmobile.inventory.compose.models.Audit

interface AuditRepository {

    suspend fun getAuditsByProjectId(projectId: String) : List<Audit>

    suspend fun saveAudit(audit: Audit)

    suspend fun deleteAudit(auditId: String) : Boolean
}