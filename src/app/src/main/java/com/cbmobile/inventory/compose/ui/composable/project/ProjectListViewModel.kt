package com.cbmobile.inventory.compose.ui.composable.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.models.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectListViewModel (private val projectRepository: ProjectRepository)
    : ViewModel() {

    private val _projects: MutableLiveData<List<Project>> by lazy {
        MutableLiveData<List<Project>>()
    }
    val projects: LiveData<List<Project>> get() = _projects

    private val _loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val isLoading: LiveData<Boolean> get() = _loading

    init {
        viewModelScope.launch(Dispatchers.IO){
            _loading.postValue(true)
            projectRepository.initializeDatabase()
            val projectsData = projectRepository.getProjects()
            when {
                projectsData.any() -> {
                    _projects.postValue(projectsData)
                }
            }
            _loading.postValue(false)
        }
    }

    val deleteProject: (String) -> Boolean = { projectId: String ->
        var didDelete = false
        viewModelScope.launch(Dispatchers.IO){
            didDelete = projectRepository.deleteProject(projectId)
            if (didDelete){
                val newProjects = _projects.value?.filter { p -> p.projectId != projectId }
                _projects.postValue(newProjects)
            }
        }
        didDelete
    }
}