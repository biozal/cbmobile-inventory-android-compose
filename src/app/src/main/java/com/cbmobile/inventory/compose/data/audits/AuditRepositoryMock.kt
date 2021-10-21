package com.cbmobile.inventory.compose.data.audits

import com.cbmobile.inventory.compose.models.Audit
import kotlinx.coroutines.flow.Flow

class AuditRepositoryMock : AuditRepository {

    override fun getAuditsByProjectId(projectId: String): Flow<List<Audit>>? {
        TODO("Not yet implemented")
    }

    override suspend fun getAudit(projectId: String, auditId: String): Audit {
        TODO("Not yet implemented")
    }

    override suspend fun saveAudit(audit: Audit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAudit(auditId: String) : Boolean {
        TODO("Not yet implemented")
    }
}