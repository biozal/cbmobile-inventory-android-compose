package com.cbmobile.inventory.compose.models

import java.util.*

data class ProjectWrapper(
    val project: Project
)

data class Project (
    var projectId: String,
    var name: String,
    var description: String,
    var isComplete: Boolean,
    var type: String,
    var dueDate: Date?,
    var locations: List<Location>?
)