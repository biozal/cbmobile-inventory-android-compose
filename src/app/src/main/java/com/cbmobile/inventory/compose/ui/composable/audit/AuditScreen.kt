package com.cbmobile.inventory.compose.ui.composable.audit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import com.cbmobile.inventory.compose.data.audits.AuditRepository
import com.cbmobile.inventory.compose.models.Audit
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

@Composable
fun AuditEditorScreen(
    openDrawer: () -> Unit,
    projectId: String?,
    auditJson: String?,
    auditRepository: AuditRepository,
    navigateUp: () -> Unit)
{
    val viewModel = AuditViewModel(
            projectId = projectId,
            auditJson =  auditJson,
            auditRepository = auditRepository)
    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(topBar = { InventoryAppBar(title = "Audit Editor", navigationIcon = Icons.Filled.ArrowBack, navigationOnClick = { navigateUp() }) })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                AuditEditor(
                    audit = viewModel.audit.value,
                    count = viewModel.count.value,
                    onNameChanged = viewModel.onNameChanged,
                    onCountChanged = viewModel.onCountChanged,
                    onNotesChanged = viewModel.onNotesChanged,
                    onPartNumberChanged = viewModel.onPartNumberChanged,
                    onSaveAudit = viewModel.onSaveAudit,
                    navigateUp = navigateUp)
            }
        }
    }
}
@Composable
fun AuditEditor(
    audit: Audit,
    count: String,
    onNameChanged: (String) -> Unit,
    onCountChanged: (String) -> Unit,
    onNotesChanged: (String) -> Unit,
    onPartNumberChanged: (String) -> Unit,
    onSaveAudit: () -> Unit,
    navigateUp: () -> Unit)
{
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)) {
        item {
            OutlinedTextField(
                value = audit.name,
                onValueChange = onNameChanged,
                label = { Text(text = "Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
        item {
            OutlinedTextField(
                value = audit.partNumber,
                onValueChange = onPartNumberChanged,
                label = { Text(text = "Part Number/SKU") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
        item {
            OutlinedTextField(
                value = count,
                onValueChange = onCountChanged,
                label = { Text(text = "Count") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
        item {
            OutlinedTextField(
                value = audit.notes,
                onValueChange = onNotesChanged,
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
        item {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(modifier = Modifier.padding(top = 8.dp), onClick = {
                    onSaveAudit()
                    navigateUp()
                })
                {
                    Text("Save", style = MaterialTheme.typography.h5)
                }
            }
        }
    }
}