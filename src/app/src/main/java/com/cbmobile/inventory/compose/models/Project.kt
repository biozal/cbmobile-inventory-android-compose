package com.cbmobile.inventory.compose.models

data class Project (
    var projectId: String,
    var name: String,
    var description: String,
    var isComplete: Boolean,
    var type: String,
    var locations: List<Location>?
)