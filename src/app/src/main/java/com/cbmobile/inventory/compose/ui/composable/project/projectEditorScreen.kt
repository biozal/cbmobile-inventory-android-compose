package com.cbmobile.inventory.compose.ui.composable.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope

import com.cbmobile.inventory.compose.data.projects.*
import com.cbmobile.inventory.compose.models.Project
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme
import java.util.*

@Composable
fun ProjectEditorScreen(
    projectJson: String?,
    projectRepository: ProjectRepository,
    navigateUp: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    lifecycleScope: LifecycleCoroutineScope) {

    val viewModel = ProjectViewModel(projectJson, projectRepository)

    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(scaffoldState = scaffoldState,
            topBar = { InventoryAppBar(title = "Project Editor") })
            {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ProjectEditor(
                        viewModel.project.value,
                        viewModel.onNameChanged,
                        viewModel.onDescriptionChanged,
                        viewModel.onSaveProject,
                        navigateUp
                    )
                }
            }
    }
}

@Composable
fun ProjectEditor(
    project: Project,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveProject: () -> Unit,
    navigateUp: () -> Unit)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)) {
        OutlinedTextField(
            value = project.name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp))
        OutlinedTextField(
            value = project.description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp))
        Row(modifier =
        Modifier.fillMaxWidth()){
            TextField(value = "",
                onValueChange = { } )
        }
        Column(modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(modifier = Modifier.padding(top = 32.dp), onClick = {
                val resultMessage = onSaveProject()
                    navigateUp()
            })
            {
                Text("Save", style = MaterialTheme.typography.h5)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectEditorPreview() {
    val project: Project = Project()
    val onNameChange: (String) -> Unit = {}
    val onDescriptionChange: (String) -> Unit = { }
    val onSaveProject: () -> Unit = {}
    val navigateUp: () -> Unit = {}

    InventoryTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
       ) {
            ProjectEditor(project, onNameChange, onDescriptionChange, onSaveProject, navigateUp)
        }
    }
}