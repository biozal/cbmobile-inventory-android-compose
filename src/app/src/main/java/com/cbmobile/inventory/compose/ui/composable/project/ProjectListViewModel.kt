package com.cbmobile.inventory.compose.ui.composable.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.models.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class ProjectListViewModel (private val projectRepository: ProjectRepository)
    : ViewModel() {

    var flow: Flow<List<Project>>? = null

    private val _projects: MutableLiveData<List<Project>> by lazy {
        MutableLiveData<List<Project>>()
    }
    val projects: LiveData<List<Project>> get() = _projects

    private val _loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val isLoading: LiveData<Boolean> get() = _loading

    val deleteProject: (String) -> Boolean = { projectId: String ->
        var didDelete = false
        viewModelScope.launch(Dispatchers.IO){
            didDelete = projectRepository.deleteProject(projectId)
        }
        didDelete
    }

   fun deleteProjects(){
       viewModelScope.launch {
           flow = null
           _projects.postValue(mutableListOf<Project>())
       }
   }

   fun setup(){
       viewModelScope.launch(Dispatchers.IO) {
           projectRepository.initializeDatabase()
           flow = projectRepository.getProjectsFlow()
           flow?.let { f ->
               f.collect { projectResults ->
                   _projects.postValue(projectResults)
               }
           }
       }
   }
}