package com.cbmobile.inventory.compose.data.projects

import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.Project

interface ProjectRepository {

    suspend fun completeProject(projectId: String)

    suspend fun saveProject(project: Project)

    suspend fun getProject(projectId: String): Project

    suspend fun deleteProject(projectId: String) : Boolean

    suspend fun getProjects(): List<Project>

    suspend fun getLocations(): List<Location>

    suspend fun initializeDatabase()
}