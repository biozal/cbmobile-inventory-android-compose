package com.cbmobile.inventory.compose.data.projects

import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.Project
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectRepositoryMock : ProjectRepository {

    override suspend fun saveProject(project: Project) : Boolean {
        TODO("Not yet implemented")
    }
    override suspend fun getProject(projectId: String): Project {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(projectId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocations(): List<Location> {
        TODO("Not yet implemented")
    }

    override suspend fun initializeDatabase() {
        TODO("Not yet implemented")
    }

}