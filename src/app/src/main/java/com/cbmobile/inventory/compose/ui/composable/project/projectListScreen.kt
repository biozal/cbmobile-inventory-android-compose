package com.cbmobile.inventory.compose.ui.composable.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import java.util.UUID
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepositoryMock
import com.cbmobile.inventory.compose.models.Project
import com.cbmobile.inventory.compose.ui.composable.HorizontalDottedProgressBar
import com.cbmobile.inventory.compose.ui.composable.NoItemsFound
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

@Composable
fun ProjectListScreen(
    projectRepository: ProjectRepository,
    navigateToProjectEditor: (String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()) {

    val viewModel = ProjectListViewModel(projectRepository)
    val projects = viewModel.projects.observeAsState(ArrayList<Project>())
    val isLoading = viewModel.isLoading.observeAsState(true)

    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(scaffoldState = scaffoldState,
            floatingActionButton = { AddButton(navigateToProjectEditor) })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                ProjectList(projects.value,
                            isLoading.value,
                            navigateToProjectEditor,
                            viewModel.deleteProject)
            }
        }
    }
}

@Composable
fun ProjectList(
    items: List<Project>,
    isLoading: Boolean,
    onEditChange: (String) -> Unit,
    onDeleteChange: (String) -> Boolean)
{

    if (isLoading && items.isEmpty()) {
        HorizontalDottedProgressBar()
    }
    else if (items.isEmpty()){
        NoItemsFound()
    }
    else {
        LazyColumn(
            modifier = Modifier .padding(16.dp)) {
            for (project in items) {
                item {  
                    ProjectCard(project, onEditChange, onDeleteChange)
                    Spacer(modifier = Modifier.padding(top = 30.dp))
                }
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project,
                onEditChange: (String) -> Unit,
                onDeleteChange: (String) -> Boolean)
{
    var expanded by remember { mutableStateOf(false) }
    Card(
        shape = MaterialTheme.shapes.small,
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .padding(top = 6.dp, bottom = 6.dp)
            .fillMaxWidth(),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .height(160.dp)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            )
            {
                Text(
                    modifier = Modifier.fillMaxWidth(0.85f)
                        .wrapContentWidth(Alignment.Start)
                        .padding(top = 10.dp),
                    text = project.name,
                    style = MaterialTheme.typography.h6
                )
                Box(
                    modifier = Modifier.fillMaxWidth()
                            .align(Alignment.CenterVertically)
                            .wrapContentSize(Alignment.TopEnd)
                )
                {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false })
                    {
                        DropdownMenuItem(onClick = {
                            onEditChange(project.projectId)
                        }) {
                            Text("Edit")
                        }
                        DropdownMenuItem(onClick = {
                            val results = onDeleteChange(project.projectId)
                            //TODO bring up snackbar with message to show we deleted
                            expanded = false
                            android.util.Log.i("DELETE-STATUS", results.toString())
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
            Row( modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = project.description,
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.DarkGray
                )
            }
        }
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
    val navigateToProjectEditor: (String) -> Unit = { _ : String -> }
    val viewModel = ProjectListViewModel(ProjectRepositoryMock())
    InventoryTheme {
        Scaffold(floatingActionButton = { AddButton(navigateToProjectEditor) })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                ProjectList(
                    viewModel.projects.value!!,
                    viewModel.isLoading.value!!,
                    navigateToProjectEditor,
                    viewModel.deleteProject)
            }
        }
    }
}