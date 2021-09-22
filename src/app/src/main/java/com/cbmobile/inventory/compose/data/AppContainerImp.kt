package com.cbmobile.inventory.compose.data

import android.content.Context
import android.location.Location
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.data.audits.AuditRepositoryDb
import com.cbmobile.inventory.compose.data.location.LocationRepository
import com.cbmobile.inventory.compose.data.location.LocationRepositoryDb

import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepositoryDb
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val projectRepository: ProjectRepository
    val auditRepository: AuditRepository
    val locationRepository: LocationRepository
}

@OptIn(InternalCoroutinesApi::class)
class AppContainerImp(private val applicationContext: Context) : AppContainer {

    override val projectRepository: ProjectRepository by lazy {
        ProjectRepositoryDb(applicationContext)
    }

    override val auditRepository: AuditRepository by lazy {
        AuditRepositoryDb(applicationContext)
    }

    override val locationRepository: LocationRepository by lazy {
        LocationRepositoryDb(applicationContext)
    }
}