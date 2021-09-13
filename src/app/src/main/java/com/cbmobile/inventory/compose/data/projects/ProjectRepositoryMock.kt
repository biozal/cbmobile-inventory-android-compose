package com.cbmobile.inventory.compose.data.projects

import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.Project
import com.couchbase.lite.DocumentChange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectRepositoryMock : ProjectRepository {

    override val documentChanges: Flow<DocumentChange>?
        get() = TODO("Not yet implemented")

    override suspend fun completeProject(projectId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveProject(project: Project) {
        TODO("Not yet implemented")
    }
    override suspend fun getProject(projectId: String): Project {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(projectId: String) : Boolean {
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