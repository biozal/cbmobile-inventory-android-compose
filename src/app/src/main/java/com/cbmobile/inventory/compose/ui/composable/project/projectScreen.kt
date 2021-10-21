package com.cbmobile.inventory.compose.ui.composable.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.cbmobile.inventory.compose.data.location.LocationRepository
import com.cbmobile.inventory.compose.data.projects.*
import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.Project
import com.cbmobile.inventory.compose.models.UserProfile
import com.cbmobile.inventory.compose.ui.composable.components.DatePicker
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.composable.components.LocationListSelection
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

@Composable
fun ProjectEditorScreen(
    currentUser: UserProfile,
    projectJson: String?,
    projectRepository: ProjectRepository,
    locationRepository: LocationRepository,
    navigateUp: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()) {

    val viewModel = ProjectViewModel(
                        currentUser = currentUser,
                        projectJson = projectJson,
                        projectRepository = projectRepository,
                        locationRepository = locationRepository)

    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(scaffoldState = scaffoldState,
            topBar = { InventoryAppBar(title = "Project Editor", navigationIcon = Icons.Filled.ArrowBack, navigationOnClick = { navigateUp() }) })
            {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ProjectEditor(
                        project = viewModel.projectState.value,
                        dueDate = viewModel.dueDateState.value,
                        locationSelection = viewModel.locationSelectionState.value,
                        onNameChange = viewModel.onNameChanged,
                        onDescriptionChange = viewModel.onDescriptionChanged,
                        onLocationChanged = viewModel.onLocationChanged,
                        onDateChanged = viewModel.onDateChanged,
                        onSaveProject = viewModel.onSaveProject,
                        locations = viewModel.locationsState,
                        navigateUp = navigateUp
                    )
                }
            }
    }
}

@Composable
fun ProjectEditor(
    project: Project,
    dueDate: String,
    locationSelection: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onLocationChanged: (Location) -> Unit,
    onDateChanged: (Long?) -> Unit,
    onSaveProject: () -> Unit,
    locations: List<Location>,
    navigateUp: () -> Unit)
{
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)) {
        item {
            OutlinedTextField(
                value = project.name,
                onValueChange = onNameChange,
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
        item {
            OutlinedTextField(
                value = project.description,
                onValueChange = onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
        item {
            LocationListSelection(locationSelection, locations, onLocationChanged)
        }
        item {
            DatePicker(selectedDate = dueDate, onDateChanged = onDateChanged)
        }
        item {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(modifier = Modifier.padding(top = 8.dp), onClick = {
                    onSaveProject()
                    navigateUp()
                })
                {
                    Text("Save", style = MaterialTheme.typography.h5)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectEditorPreview() {
    val project = Project()
    val onNameChange: (String) -> Unit = {}
    val dueDate = "Due Date"
    val locationSelect = "Select Location"
    val onDescriptionChange: (String) -> Unit = { }
    val onSaveProject: () -> Unit = {}
    val navigateUp: () -> Unit = {}
    val onLocationChanged: (Location) -> Unit = {}
    val onDateChanged: (Long?) -> Unit = {}
    val locations = mutableListOf<Location>()

    InventoryTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
       ) {
            ProjectEditor(project = project,
                locationSelection = locationSelect,
                onNameChange = onNameChange,
                onDescriptionChange = onDescriptionChange,
                onSaveProject = onSaveProject,
                locations = locations,
                onLocationChanged = onLocationChanged,
                navigateUp = navigateUp,
                dueDate = dueDate,
                onDateChanged = onDateChanged)
        }
    }
}