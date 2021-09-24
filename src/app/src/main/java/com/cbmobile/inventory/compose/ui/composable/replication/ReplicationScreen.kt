package com.cbmobile.inventory.compose.ui.composable.replication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

@Composable
fun ReplicationScreen(
    openDrawer: () -> Unit,
    replicationConfigNav: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    lifecycleScope: LifecycleCoroutineScope)
{
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
                ReplicationStatus()
            }
        }
    }
}

@Composable
fun ReplicationStatus() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        item {
            Text("Hello Replication")
        }
    }
}