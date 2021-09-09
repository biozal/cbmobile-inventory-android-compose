package com.cbmobile.inventory.compose.ui.composable.project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.util.UUID
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepositoryDb
import com.cbmobile.inventory.compose.data.projects.ProjectRepositoryMock
import com.cbmobile.inventory.compose.ui.composable.MainDestinations
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

@Composable
fun ProjectListScreen(
    projectRepository: ProjectRepository,
    navigateToProjectEditor: (String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()) {

    val viewModel = ProjectListViewModel(projectRepository)

    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(scaffoldState = scaffoldState,
            floatingActionButton = { AddButton(navigateToProjectEditor) })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                ProjectList(viewModel)
            }
        }
    }
}

@Composable
fun ProjectList(viewModel: ProjectListViewModel) {
    Column() {
        Text(text = "List Projects Here!")
    }
}

@Composable
fun AddButton(navigateToProjectEditor: (String) -> Unit) {
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = FloatingActionButtonDefaults.elevation(),
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
        onClick = {
            navigateToProjectEditor(UUID.randomUUID().toString())
        })
    {
        Icon(
            Icons.Default.Add,
            contentDescription = "add project"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProjectListScreenPreview() {
    val navigateToProjectEditor: (String) -> Unit = { projectId: String -> }
    val viewModel = ProjectListViewModel(ProjectRepositoryMock())
    InventoryTheme {
        Scaffold(floatingActionButton = { AddButton(navigateToProjectEditor) })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                ProjectList(viewModel)
            }
        }
    }
}