package com.cbmobile.inventory.compose.ui.composable.audit

import androidx.lifecycle.ViewModel
import com.cbmobile.inventory.compose.data.audits.AuditRepository

class AuditListViewModel(
    private val projectId: String?,
    private val auditRepository: AuditRepository): ViewModel() {

}