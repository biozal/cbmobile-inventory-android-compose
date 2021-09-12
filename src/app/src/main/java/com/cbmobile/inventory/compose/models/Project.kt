package com.cbmobile.inventory.compose.models

import java.util.*

class ProjectWrapper(override var item: Project) : Wrapper<Project> {
}

data class Project (
    var projectId: String = "",
    var name: String = "",
    var description: String = "",
    var isComplete: Boolean = false,
    var type: String = "project",
    var dueDate: Date? = null,
    var locations: List<Location>? = null
)