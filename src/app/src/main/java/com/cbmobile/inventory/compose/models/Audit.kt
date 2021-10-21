package com.cbmobile.inventory.compose.models

import java.util.*

class AuditModelDTO(override var item: Audit) : ModelDTO<Audit>

data class Audit (
    var auditId: String = "",
    var projectId: String = "",
    var name: String = "",
    var count: Int = 0,
    var type: String = "audit",
    var notes: String = "",
    var partNumber: String = "",
    var pictureMetadata: String? = null,
    //security tracking
    var team: String = "",
    var createdBy: String = "",
    var modifiedBy: String = "",
    var createdOn: Date? = null,
    var modifiedOn: Date? = null
)