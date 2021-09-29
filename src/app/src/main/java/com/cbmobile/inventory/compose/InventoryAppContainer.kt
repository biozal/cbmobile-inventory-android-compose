package com.cbmobile.inventory.compose

import android.content.Context
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.data.audits.AuditRepositoryDb
import com.cbmobile.inventory.compose.data.location.LocationRepository
import com.cbmobile.inventory.compose.data.location.LocationRepositoryDb

import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepositoryDb
import com.cbmobile.inventory.compose.data.replication.ReplicationService
import com.cbmobile.inventory.compose.data.replication.ReplicationServiceDb
import com.cbmobile.inventory.compose.services.AuthenticationService
import com.cbmobile.inventory.compose.services.LocalAuthenticationService
import com.cbmobile.inventory.compose.ui.composable.developer.DeveloperViewModel
import com.cbmobile.inventory.compose.ui.composable.project.ProjectListViewModel
import com.cbmobile.inventory.compose.ui.composable.replication.ReplicationConfigViewModel
import com.cbmobile.inventory.compose.ui.composable.replication.ReplicationViewModel
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val projectRepository: ProjectRepository
    val auditRepository: AuditRepository
    val locationRepository: LocationRepository
    val replicationService: ReplicationService
    val inventoryDatabase: InventoryDatabase
    val authenticationService: AuthenticationService
    val replicationConfigViewModel: ReplicationConfigViewModel
    val replicationViewModel: ReplicationViewModel

    @InternalCoroutinesApi
    val projectListViewModel: ProjectListViewModel

    @InternalCoroutinesApi
    val developerViewModel: DeveloperViewModel
}

@OptIn(InternalCoroutinesApi::class)
class InventoryAppContainer(private val applicationContext: Context)
    : AppContainer {

    override val inventoryDatabase: InventoryDatabase by lazy {
        InventoryDatabase.getInstance(applicationContext)
    }

    override val projectRepository: ProjectRepository by lazy {
        ProjectRepositoryDb(inventoryDatabase = inventoryDatabase,
            locationRepository = locationRepository)
    }

    override val auditRepository: AuditRepository by lazy {
        AuditRepositoryDb(context = applicationContext)
    }

    override val locationRepository: LocationRepository by lazy {
        LocationRepositoryDb(context = applicationContext)
    }

    override val replicationService: ReplicationService by lazy {
        ReplicationServiceDb(inventoryDatabase = inventoryDatabase)
    }

    override val authenticationService: AuthenticationService by lazy {
        LocalAuthenticationService(inventoryDatabase = inventoryDatabase)
    }

    /* ViewModel's */
    override val replicationViewModel: ReplicationViewModel by lazy {
        ReplicationViewModel(replicationService = replicationService)
    }

    override val replicationConfigViewModel: ReplicationConfigViewModel by lazy {
        ReplicationConfigViewModel(replicationService = replicationService)
    }

    override val projectListViewModel: ProjectListViewModel by lazy {
        ProjectListViewModel(projectRepository = projectRepository)
    }

    override val developerViewModel: DeveloperViewModel by lazy {
        DeveloperViewModel(
            projectRepository = projectRepository,
            projectListViewModel = projectListViewModel)
    }

}