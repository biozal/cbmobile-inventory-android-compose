package com.cbmobile.inventory.compose.ui.composable.audit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.models.Project
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.*

class AuditListViewModel(
    private val projectJson: String?,
    private val projectRepository: ProjectRepository,
    private val auditRepository: AuditRepository): ViewModel()
{
    var project = mutableStateOf(Project())
    init {
        viewModelScope.launch{
            if (projectJson != null) {
                project.value = Gson().fromJson(projectJson, Project::class.java)
            }
        }
    }
}