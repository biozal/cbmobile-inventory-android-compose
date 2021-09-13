package com.cbmobile.inventory.compose.data.audits

import com.cbmobile.inventory.compose.models.Audit
import com.cbmobile.inventory.compose.models.Project

interface AuditRepository {

    suspend fun getAuditsByProjectId(projectId: String) : List<Audit>

    suspend fun getAudit(auditId: String): Audit

    suspend fun saveAudit(audit: Audit)

    suspend fun deleteAudit(auditId: String) : Boolean
}