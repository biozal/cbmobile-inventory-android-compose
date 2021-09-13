package com.cbmobile.inventory.compose.ui.composable.audit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleCoroutineScope
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.ui.composable.NoItemsFound
import com.cbmobile.inventory.compose.ui.composable.components.AddButton
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.composable.project.ProjectList
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

@Composable
fun AuditListScreen(
    project: String?,
    auditRepository: AuditRepository,
    projectRepository: ProjectRepository,
    navigateUp: () -> Unit,
    navigateToAuditEditor: (String) -> Unit,
    lifecycleScope: LifecycleCoroutineScope)
{
    val viewModel = AuditListViewModel(
        project,
        projectRepository,
        auditRepository)

    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(topBar = { InventoryAppBar(title = "${viewModel.project.value.name} Audits")},
            floatingActionButton = { AddButton(navigateToAuditEditor) })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                AuditList()
            }
        }
    }
}

@Composable
fun AuditList( ){
    NoItemsFound()
}