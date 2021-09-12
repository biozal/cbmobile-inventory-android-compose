package com.cbmobile.inventory.compose.models


data class AuditWrapper (
    val audit: Audit
)

data class Audit (
    var auditId: String,
    var projectId: String,
    var name: String,
    var count: Int,
    var type: String,
    var notes: String?,
    var partNumber: String?,
)