package com.cbmobile.inventory.compose.ui.composable.project

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.Project
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.*

class ProjectViewModel(private val projectJson: String?,
                       private val projectRepository: ProjectRepository)
    : ViewModel() {
    var project = mutableStateOf(Project())
    val locations = mutableListOf<Location>()

    init {
        viewModelScope.launch {
            val results = projectRepository.getLocations()
            for (result in results) {
                locations.add(result)
            }
            if (projectJson == null || projectJson == "create") {
                project.value = projectRepository.getProject(UUID.randomUUID().toString())
            } else {
                project.value = Gson().fromJson(projectJson, Project::class.java)
            }
        }
    }

    val onNameChanged: (String) -> Unit = { newValue ->
        val p = project.value.copy()
        p.name = newValue
        project.value = p
    }

    val onDescriptionChanged: (String) -> Unit = { newValue ->
        val p = project.value.copy()
        p.description = newValue
        project.value = p
    }

    val onSaveProject: () -> Unit = {
        viewModelScope.launch {
            project.value.name = project.value.name.trim()
            project.value.description = project.value.description.trim()
            val results = projectRepository.saveProject(project.value)
       }
    }
}