package com.cbmobile.inventory.compose.ui.composable.audit

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.models.Audit
import com.cbmobile.inventory.compose.models.Project
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuditListViewModel(
    private val projectJson: String?,
    private val projectRepository: ProjectRepository,
    private val auditRepository: AuditRepository): ViewModel()
{
    var project = mutableStateOf(Project())
    var flow: Flow<List<Audit>>? = null

    private val _audits: MutableLiveData<List<Audit>> by lazy {
        MutableLiveData<List<Audit>>()
    }
    val audits: LiveData<List<Audit>> get() = _audits

    init {
        viewModelScope.launch {
            if (projectJson != null) {
                project.value = Gson().fromJson(projectJson, Project::class.java)
                flow = auditRepository.getAuditsByProjectId(project.value.projectId)
                flow?.let { f ->
                    f.collect {
                       _audits.postValue(it)
                    }
                }
            }
        }
    }

    val deleteAudit: (String) -> Boolean = { auditId: String ->
       var didDelete = false
        viewModelScope.launch(Dispatchers.IO) {
            didDelete = auditRepository.deleteAudit(auditId)
        }
        didDelete
    }
}