package com.cbmobile.inventory.compose.ui.composable.developer

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.ui.composable.project.ProjectListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class DeveloperViewModel(
    private val projectRepository: ProjectRepository,
    private val projectListViewModel: ProjectListViewModel,
) : ViewModel() {

    val logMessages = mutableStateListOf("")

    fun onDeleteDatabase(){
        viewModelScope.launch(Dispatchers.IO){
            logMessages.add("Starting to delete database")
            projectRepository.deleteDatabase()
            logMessages.add("Database deleted - creating new database")
            projectRepository.initializeDatabase()
            logMessages.add("database created - updating Project List ViewModel")
            projectListViewModel.deleteProjects()
            logMessages.add("ViewModel updated - new flow created")
        }
    }

    fun onLoadSampleData() {
        viewModelScope.launch(Dispatchers.IO){
            logMessages.add("Starting to load sample data")
            projectRepository.loadSampleData()
            logMessages.add("Sample data loaded")
        }
    }
}