package com.cbmobile.inventory.compose.ui.composable.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.lifecycleScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleCoroutineScope

import com.cbmobile.inventory.compose.data.projects.*
import com.cbmobile.inventory.compose.models.Project
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ProjectEditorScreen(
    projectId: String?,
    projectRepository: ProjectRepository,
    navigateUp: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    lifecycleScope: LifecycleCoroutineScope) {

    val viewModel = ProjectViewModel(projectId, projectRepository)

    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {
            ProjectEditor(
                viewModel.project.value,
                viewModel.onNameChanged,
                viewModel.onDescriptionChanged,
                viewModel.onSaveProject,
                lifecycleScope,
                navigateUp)
        }
    }
}

@Composable
fun ProjectEditor(
                project: Project,
                onNameChange: (String) -> Unit,
                onDescriptionChange: (String) -> Unit,
                onSaveProject: () -> Unit,
                lifecycleScope: LifecycleCoroutineScope,
                navigateUp: () -> Unit){
    //val snackbarHostState = remember{SnackbarHostState()}
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
                .padding(bottom = 10.dp)
        )
        OutlinedTextField(
            value = project.description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Column(modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(modifier = Modifier.padding(top = 32.dp), onClick = {
                val resultMessage = onSaveProject()
                    /* lifecycleScope.launch {
                        snackbarHostState.showSnackbar(
                            message = resultMessage,
                            actionLabel = "",
                            duration = SnackbarDuration.Short
                        )
                    } */
                    navigateUp()
                    })
            {
                Text("Save", style = MaterialTheme.typography.h5)
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun ProjectEditorPreview() {
    val viewModel = ProjectViewModel(UUID.randomUUID().toString(), ProjectRepositoryMock())
    InventoryTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
       ) {
            ProjectEditor(viewModel, null)
        }
    }
}
 */