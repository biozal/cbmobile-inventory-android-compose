package com.cbmobile.inventory.compose.models

import java.util.*

class ProjectDTO(override var item: Project) : DTO<Project> {
}

data class Project (
    var projectId: String = "",
    var name: String = "",
    var description: String = "",
    var isComplete: Boolean = false,
    var type: String = "project",
    var dueDate: Date? = null,
    var location: Location? = null
)

