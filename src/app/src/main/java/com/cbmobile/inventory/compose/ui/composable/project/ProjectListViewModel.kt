package com.cbmobile.inventory.compose.ui.composable.project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.models.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@InternalCoroutinesApi
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
        viewModelScope.launch {
            projectRepository.initializeDatabase()
            val flow = projectRepository.getProjectsFlow()
            flow?.let { f ->
                f.collect { projectResults ->
                    _projects.postValue(projectResults)
                }
            }
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