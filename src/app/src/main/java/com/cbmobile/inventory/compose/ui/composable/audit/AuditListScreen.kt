package com.cbmobile.inventory.compose.ui.composable.audit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cbmobile.inventory.compose.models.Audit
import com.cbmobile.inventory.compose.ui.composable.components.NoItemsFound
import com.cbmobile.inventory.compose.ui.composable.components.AddButton
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AuditListScreen(
    viewModel: AuditListViewModel,
    navigateUp: () -> Unit,
    navigateToAuditEditor: (String, String) -> Unit,
    snackBarCoroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState)
{
    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { InventoryAppBar(
                            title = "${viewModel.project.value.name} Audits",
                            navigationIcon = Icons.Filled.ArrowBack,
                            navigationOnClick = { navigateUp() })
            },
            floatingActionButton = {
                AddButton(
                    onClick = {
                        navigateToAuditEditor(viewModel.project.value.projectId, "create")
                })
            })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                var audits = viewModel.audits.observeAsState()
                AuditList(
                    items = audits?.value,
                    onEditChange = navigateToAuditEditor,
                    onDeleteChange = viewModel.deleteAudit,
                    snackBarCoroutineScope = snackBarCoroutineScope,
                    scaffoldState = scaffoldState)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AuditList(
    items: List<Audit>?,
    onEditChange: (String, String) -> Unit,
    onDeleteChange:(String) -> Boolean,
    snackBarCoroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState) {
    if (items == null || items?.count() == 0) {
        NoItemsFound()
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            for(audit in items) {
                item {
                    AuditCard(
                        audit = audit,
                        onEditChange = onEditChange,
                        onDeleteChange = onDeleteChange,
                        snackBarCoroutineScope = snackBarCoroutineScope,
                        scaffoldState = scaffoldState
                    )
                    Spacer(modifier = Modifier.padding(top = 24.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AuditCard(audit: Audit,
                onEditChange: (String, String) -> Unit,
                onDeleteChange: (String) -> Boolean,
                snackBarCoroutineScope: CoroutineScope,
                scaffoldState: ScaffoldState)
{
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.small,
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .padding(top = 6.dp, bottom = 6.dp)
            .fillMaxWidth(),
        elevation = 8.dp,
        onClick = {
            val auditJson = Gson().toJson(audit)
            onEditChange(audit.projectId, auditJson)
        }
    ){
        Column(
            modifier = Modifier
                .height(180.dp)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .wrapContentWidth(Alignment.Start),
                    text = audit.name,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                        .wrapContentSize(Alignment.TopEnd)
                )
                {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false })
                    {
                        DropdownMenuItem(onClick = {
                            val auditJson = Gson().toJson(audit)
                            onEditChange(audit.projectId, auditJson)
                        }) {
                            Text("Edit")
                        }
                        DropdownMenuItem(onClick = {
                            val results = onDeleteChange(audit.auditId)
                            expanded = false
                            if (!results) {
                                snackBarCoroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("The audit was deleted from database")
                                }
                            }
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
            audit.auditId?.let {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Id:",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface)
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        overflow = TextOverflow.Ellipsis,
                        text = it,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
            audit.partNumber?.let {
                if (it.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SKU/Part #:",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface
                        )

                        Text(
                            modifier = Modifier.padding(start = 6.dp),
                            text = it,
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
            audit.count?.let {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Text(text = "Count:",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface)

                    Text(modifier = Modifier.padding(start = 6.dp),
                        text = it.toString(),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
            audit.notes?.let {
                if (it.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Notes:",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            overflow = TextOverflow.Ellipsis,
                            text = it,
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        }
    }
}
