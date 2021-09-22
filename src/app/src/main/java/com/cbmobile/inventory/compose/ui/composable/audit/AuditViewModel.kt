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
    private val auditJson: String?,
    private val auditRepository: AuditRepository)
    : ViewModel()
{
    var audit = mutableStateOf(Audit())

    init {
        viewModelScope.launch{
            if (auditJson == null || auditJson == "create"){
                audit.value = auditRepository.getAudit(UUID.randomUUID().toString())
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

    val onCountChanged: (Int) -> Unit = { newValue ->
        val a = audit.value.copy()
        a.count = newValue
        audit.value = a
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
            //clean up data - remove spaces at the end of strings
            audit.value.name = audit.value.name.trim()
            audit.value.notes?.let { notes ->
                audit.value.notes = notes.trim()
            }
            audit.value.partNumber?.let { partNumber ->
                audit.value.partNumber = partNumber.trim()
            }
            auditRepository.saveAudit(audit.value)
        }
    }

}