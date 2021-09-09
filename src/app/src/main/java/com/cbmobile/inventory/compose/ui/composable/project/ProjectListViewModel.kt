package com.cbmobile.inventory.compose.ui.composable.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectListViewModel (val projectRepository: ProjectRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO){
            projectRepository.initializeDatabase()
        }
    }
}