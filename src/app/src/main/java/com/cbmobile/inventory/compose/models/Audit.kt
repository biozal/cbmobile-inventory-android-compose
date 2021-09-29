package com.cbmobile.inventory.compose.models


data class AuditWrapper (
    val audit: Audit
)

data class Audit (
    var auditId: String = "",
    var projectId: String = "",
    var name: String = "",
    var count: Int = 0,
    var type: String = "audit",
    var notes: String = "",
    var partNumber: String = "",
    var pictureMetadata: String? = null,
    var team: String = ""
)