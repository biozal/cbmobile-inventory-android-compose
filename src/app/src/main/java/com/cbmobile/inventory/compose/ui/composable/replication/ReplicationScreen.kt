package com.cbmobile.inventory.compose.ui.composable.replication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cbmobile.inventory.compose.data.replication.ReplicationServiceMock
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.composable.components.RowButton
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

@Composable
fun ReplicationScreen(
    viewModel: ReplicationViewModel,
    openDrawer: () -> Unit,
    replicationConfigNav: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState())
{
    // needed because we can go back and forth with config screen -
    // TODO find a better solution
    viewModel.updateStatus()

    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(scaffoldState = scaffoldState,
            topBar = {
                InventoryAppBar(
                    title = "Replication Sync",
                    navigationIcon = Icons.Filled.Menu,
                    navigationOnClick = { openDrawer() },
                    menuAction = {
                        IconButton(onClick = { replicationConfigNav() }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(end = 10.dp)
                            )
                        }
                    }
                )
            })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                ReplicationStatus(
                    replicationStatus = viewModel.replicationStatus.value,
                    replicationProgress = viewModel.replicationProgress.value,
                    logMessage = viewModel.logMessages,
                    isButtonShown = viewModel.isButtonActive.value,
                    onStartClick = viewModel::onStartClick,
                    onStopClick = viewModel::onStopClick,
                    onClearLogs = viewModel::clearLogs
                )
            }
        }
    }
}

@Composable
fun ReplicationStatus(
    replicationStatus: String,
    replicationProgress: String,
    logMessage: List<String>,
    isButtonShown: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onClearLogs: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        item {
            Text(text = "Replication Status: $replicationStatus")
        }
        item {
            Text(text = "Replication Progress: $replicationProgress",
                modifier = Modifier.padding(top = 10.dp))
        }
        item {
            ReplicationDivider()
        }
        if (isButtonShown) {
            item {
                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()) {
                    Button(modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        onClick = {
                            onStartClick()
                        })
                    {
                        Text("Start", style = MaterialTheme.typography.h5)
                    }
                    Button(modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        onClick = {
                            onStopClick()
                        })
                    {
                        Text("Stop", style = MaterialTheme.typography.h5)
                    }
                    Button(modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        onClick = { onClearLogs() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp, 30.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            item {
                ReplicationDivider()
            }
            logMessage.forEach {
                item {
                    Text(it)
                }
            }
        }
    }
}

@Composable
fun ReplicationDivider() {
    Divider(color = Color.DarkGray,
        thickness = 1.dp,
        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp))
}


@Preview(showBackground = true)
@Composable
fun ReplicationScreenPreview(){
    val viewModel = ReplicationViewModel(ReplicationServiceMock())
    InventoryTheme {
        ReplicationStatus(
            replicationStatus = viewModel.replicationStatus.value,
            replicationProgress = viewModel.replicationProgress.value,
            logMessage =  viewModel.logMessages,
            isButtonShown = true,
            onStartClick = viewModel::onStartClick,
            onStopClick = viewModel::onStopClick,
            onClearLogs = viewModel::clearLogs
        )
    }
}