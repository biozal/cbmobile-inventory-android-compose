package com.cbmobile.inventory.compose.ui.composable.audit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.models.Audit
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

@Composable
fun AuditEditorScreen(
    openDrawer: () -> Unit,
    auditJson: String?,
    auditRepository: AuditRepository,
    navigateUp: () -> Unit,
    lifecycleScope: LifecycleCoroutineScope)
{
    val viewModel = AuditViewModel(auditJson, auditRepository)
    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(topBar = { InventoryAppBar(title = "Audit Editor", navigationIcon = Icons.Filled.ArrowBack, navigationOnClick = { navigateUp() }) })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                AuditEditor(
                    viewModel.audit.value,
                    viewModel.onNameChanged,
                    viewModel.onCountChanged,
                    viewModel.onNotesChanged,
                    viewModel.onPartNumberChanged,
                    viewModel.onSaveAudit,
                    navigateUp)
            }
        }
    }
}
@Composable
fun AuditEditor(
    audit: Audit,
    onNameChanged: (String) -> Unit,
    onCountChanged: (Int) -> Unit,
    onNotesChanged: (String) -> Unit,
    onPartNumberChanged: (String) -> Unit,
    onSaveAudit: () -> Unit,
    navigateUp: () -> Unit)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)) {
        OutlinedTextField(
            value = audit.name,
            onValueChange = onNameChanged,
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp))
    }
}