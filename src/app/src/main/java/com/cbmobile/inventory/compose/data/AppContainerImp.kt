package com.cbmobile.inventory.compose.data

import android.content.Context

import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepositoryDb

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val projectRepository: ProjectRepository
}

class AppContainerImp(private val applicationContext: Context) : AppContainer {
    override val projectRepository: ProjectRepository by lazy {
        ProjectRepositoryDb(applicationContext)
    }
}