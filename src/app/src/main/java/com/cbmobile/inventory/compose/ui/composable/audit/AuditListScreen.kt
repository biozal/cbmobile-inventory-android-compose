package com.cbmobile.inventory.compose.ui.composable.audit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.ui.composable.components.NoItemsFound
import com.cbmobile.inventory.compose.ui.composable.components.AddButton
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

@Composable
fun AuditListScreen(
    openDrawer: () -> Unit,
    project: String?,
    auditRepository: AuditRepository,
    projectRepository: ProjectRepository,
    navigateUp: () -> Unit,
    navigateToAuditEditor: (String, String) -> Unit)
{
    val viewModel = AuditListViewModel(
        project,
        projectRepository,
        auditRepository)

    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(topBar = { InventoryAppBar(
                            title = "${viewModel.project.value.name} Audits",
                            navigationIcon = Icons.Filled.ArrowBack,
                            navigationOnClick = { navigateUp() })},
            floatingActionButton = {
                AddButton(
                    onClick = {
                        navigateToAuditEditor(viewModel.project.value.projectId, "create")
                })
            })
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