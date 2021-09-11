package com.cbmobile.inventory.compose.ui.composable.project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProjectViewModel(private val projectId: String?,
                       private val projectRepository: ProjectRepository)
    : ViewModel() {
    private var _project: Project? = null
    val projectName: MutableLiveData<String> = MutableLiveData<String>()
    val projectDescription: MutableLiveData<String> = MutableLiveData<String>()

    val locations: MutableLiveData<List<Location>> by lazy {
        MutableLiveData<List<Location>>()
    }

    init {
        viewModelScope.launch {
            val results = projectRepository.getLocations()
            locations.value = results
            if (projectId == null){
                _project = projectRepository.getProject(UUID.randomUUID().toString())
            }
            else {
                _project = projectRepository.getProject(projectId)
            }
        }
    }

    fun onProjectNameChanged(newValue: String){
        _project?.name = newValue.trim()
        projectName.value = newValue
    }

    fun onProjectDescriptionChanged(newValue: String){
        _project?.description = newValue.trim()
        projectDescription.value = newValue
    }

    fun saveProject() : String {
        var message = ""
        if (_project != null && _project!!.name != "")  {
            viewModelScope.launch {
                val results = projectRepository.saveProject(_project!!)
                message = if (results) "Project was saved" else "Error: Could not save project at this time"
            }
        } else {
            message = "Error: project name must be set before saving"
        }
        return message
    }

}