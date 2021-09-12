package com.cbmobile.inventory.compose.data.audits

import com.cbmobile.inventory.compose.models.Audit

class AuditRepositoryMock : AuditRepository {
    override suspend fun getAuditsByProjectId(projectId: String): List<Audit> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAudit(audit: Audit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAudit(auditId: String) {
        TODO("Not yet implemented")
    }
}