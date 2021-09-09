package com.cbmobile.inventory.compose.data.projects

import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.Project

interface ProjectRepository {

    suspend fun saveProject(project: Project)

    suspend fun getProject(projectId: String): Project

    suspend fun getProjects(): List<Project>

    suspend fun getLocations(): List<Location>

    suspend fun initializeDatabase()
}