package com.cbmobile.inventory.compose.ui.composable.audit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.models.Audit
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.*

class AuditViewModel(
    private val projectId: String?,
    private val auditJson: String?,
    private val auditRepository: AuditRepository)
    : ViewModel()
{
    var audit = mutableStateOf(Audit())
    var count = mutableStateOf("")
    init {
        viewModelScope.launch{
            if (auditJson == null || auditJson == "create"){
                projectId?.let {
                    audit.value = auditRepository.getAudit(
                        projectId = it,
                        auditId = UUID.randomUUID().toString()
                    )
                }
            } else {
                audit.value = Gson().fromJson(auditJson, Audit::class.java)
            }
        }
    }

    val onNameChanged: (String) -> Unit = { newValue ->
        val a = audit.value.copy()
        a.name = newValue
        audit.value = a
    }

    val onCountChanged: (String) -> Unit = { newValue ->
        val a = audit.value.copy()
        if (newValue != "") {
            val a = audit.value.copy()
            a.count = newValue.toInt()
            audit.value = a
            count.value = newValue
        }
    }

    val onNotesChanged: (String) -> Unit = { newValue ->
        val a = audit.value.copy()
        a.notes = newValue
        audit.value = a
    }

    val onPartNumberChanged: (String) -> Unit = { newValue ->
        val a = audit.value.copy()
        a.partNumber = newValue
        audit.value = a
    }

    val onSaveAudit: () -> Unit = {
        viewModelScope.launch {
            projectId?.let {
                //clean up data - remove spaces at the end of strings
                audit.value.name = audit.value.name.trim()
                audit.value.notes?.let { notes ->
                    audit.value.notes = notes.trim()
                }
                audit.value.partNumber?.let { partNumber ->
                    audit.value.partNumber = partNumber.trim()
                }
                //add in the project of the audit
                audit.value.projectId = projectId
                auditRepository.saveAudit(audit.value)
            }
        }
    }

}