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
            ProjectEditor(viewModel, lifecycleScope)
        }
    }
}

@Composable
fun ProjectEditor(viewModel: ProjectViewModel,
                  lifecycleScope: LifecycleCoroutineScope){
    val snackbarHostState = remember{SnackbarHostState()}
    val nameState = viewModel.projectName.observeAsState(initial = "")
    val descriptionState = viewModel.projectDescription.observeAsState(initial = "")
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)) {
        OutlinedTextField(
            value = nameState.value,
            onValueChange = viewModel::onProjectNameChanged,
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        OutlinedTextField(
            value = descriptionState.value,
            onValueChange = viewModel::onProjectDescriptionChanged,
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
                val resultMessage = viewModel.saveProject()
                    lifecycleScope.launch {
                        snackbarHostState.showSnackbar(
                            message = resultMessage,
                            actionLabel = "",
                            duration = SnackbarDuration.Short
                        )
                    }
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