package com.cbmobile.inventory.compose.ui.composable.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.cbmobile.inventory.compose.data.projects.ProjectRepositoryMock
import com.cbmobile.inventory.compose.models.Project
import com.cbmobile.inventory.compose.ui.composable.components.HorizontalDottedProgressBar
import com.cbmobile.inventory.compose.ui.composable.components.NoItemsFound
import com.cbmobile.inventory.compose.ui.composable.components.AddButton
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@Composable
fun ProjectListScreen(
    viewModel: ProjectListViewModel,
    openDrawer: () -> Unit,
    navigateToProjectEditor: (String) -> Unit,
    navigateToAuditListByProject: (String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    snackBarCoroutineScope: CoroutineScope) {

    val projects = viewModel.projects.observeAsState(ArrayList<Project>())
    val isLoading = viewModel.isLoading.observeAsState(true)

    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(scaffoldState = scaffoldState,
            topBar = { InventoryAppBar(title = "Projects", navigationIcon = Icons.Filled.Menu, navigationOnClick = { openDrawer() } )},
            floatingActionButton = { AddButton(navigateToProjectEditor) })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                var projects = viewModel.projects.observeAsState()
                ProjectList(projects?.value,
                            isLoading.value,
                            navigateToAuditListByProject,
                            navigateToProjectEditor,
                            viewModel.deleteProject,
                            snackBarCoroutineScope,
                            scaffoldState)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectList(
    items: List<Project>?,
    isLoading: Boolean,
    onProjectSelected: (String) -> Unit,
    onEditChange: (String) -> Unit,
    onDeleteChange: (String) -> Boolean,
    snackBarCoroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState)
{
    items?.let {
        if (isLoading && it.isEmpty()) {
            HorizontalDottedProgressBar()
        } else if (it.isEmpty()) {
            NoItemsFound()
        } else {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                for (project in it) {
                    item {
                        ProjectCard(
                            project,
                            onProjectSelected,
                            onEditChange,
                            onDeleteChange,
                            snackBarCoroutineScope,
                            scaffoldState
                        )
                        Spacer(modifier = Modifier.padding(top = 30.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectCard(project: Project,
                onProjectSelected: (String) -> Unit,
                onEditChange: (String) -> Unit,
                onDeleteChange: (String) -> Boolean,
                snackBarCoroutineScope: CoroutineScope,
                scaffoldState: ScaffoldState)
{
    var expanded by remember { mutableStateOf(false) }
    Card(
        shape = MaterialTheme.shapes.small,
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .padding(top = 6.dp, bottom = 6.dp)
            .fillMaxWidth(),
        elevation = 8.dp,
        onClick = {
            val projectJson = Gson().toJson(project)
            onProjectSelected(projectJson)
        }
    ) {
        Column(
            modifier = Modifier
                .height(200.dp)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .wrapContentWidth(Alignment.Start)
                        .padding(top = 10.dp),
                    text = project.name,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
                            val projectJson = Gson().toJson(project)
                            onEditChange(projectJson)
                        }) {
                            Text("Edit")
                        }
                        DropdownMenuItem(onClick = {
                            val results = onDeleteChange(project.projectId)
                            expanded = false
                            if (!results) {
                                snackBarCoroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("The project was deleted from database")
                                }
                            }
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
            project.location?.name?.let {
                Row(modifier = Modifier
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "",
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colors.onSurface)

                    Text(modifier = Modifier.padding(start = 6.dp),
                        text = it,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = if(project.isOverDue()) Color.Red else MaterialTheme.colors.onSurface)
                Text(modifier = Modifier.padding(start = 6.dp),
                    text = project.getDueDateString(),
                    style = MaterialTheme.typography.caption,
                    color = if (project.isOverDue()) Color.Red else MaterialTheme.colors.onSurface
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Icon(
                    Icons.Default.Group,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = if(project.isOverDue()) Color.Red else MaterialTheme.colors.onSurface)
                Text(modifier = Modifier.padding(start = 6.dp),
                    text = project.team,
                    style = MaterialTheme.typography.caption,
                    color = if (project.isOverDue()) Color.Red else MaterialTheme.colors.onSurface
                )
            }
            Row( modifier = Modifier.fillMaxWidth()) {
                Text(
                    maxLines = 3,
                    modifier = Modifier.padding(top = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    text = project.description,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

@InternalCoroutinesApi
@Preview(showBackground = true)
@Composable
fun ProjectListScreenPreview() {

    val navigateToProjectEditor: (String) -> Unit = { _ : String -> }
    val onProjectSelected: (String) -> Unit = { _ : String -> }
    val onEditChange: (String) -> Unit = { _ : String -> }
    val onDeleteChange: (String) -> Boolean  = { _: String -> false }
    val viewModel = ProjectListViewModel(ProjectRepositoryMock())
    val scaffoldState:ScaffoldState = rememberScaffoldState()
    val coRouteScope = rememberCoroutineScope()

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
                    onProjectSelected,
                    onEditChange,
                    onDeleteChange,
                    coRouteScope,
                    scaffoldState)
            }
        }
    }
}