package com.cbmobile.inventory.compose.data

import android.content.Context
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.data.audits.AuditRepositoryDb

import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepositoryDb

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val projectRepository: ProjectRepository
    val auditRepository: AuditRepository
}

class AppContainerImp(private val applicationContext: Context) : AppContainer {
    override val projectRepository: ProjectRepository by lazy {
        ProjectRepositoryDb(applicationContext)
    }
    override val auditRepository: AuditRepository by lazy {
        AuditRepositoryDb(applicationContext)
    }
}