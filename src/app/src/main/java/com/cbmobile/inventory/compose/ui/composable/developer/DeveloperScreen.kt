package com.cbmobile.inventory.compose.ui.composable.developer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cbmobile.inventory.compose.models.UserProfile
import com.cbmobile.inventory.compose.ui.composable.components.InventoryAppBar
import com.cbmobile.inventory.compose.ui.composable.project.ProjectListViewModel
import com.cbmobile.inventory.compose.ui.theme.InventoryTheme
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Composable
fun DeveloperScreen(
    viewModel: DeveloperViewModel,
    openDrawer: () -> Unit,
    currentUser: UserProfile,
    scaffoldState: ScaffoldState = rememberScaffoldState())
{
    InventoryTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(scaffoldState = scaffoldState,
            topBar = {
                InventoryAppBar(
                    title = "Developer Options",
                    navigationIcon = Icons.Filled.Menu,
                    navigationOnClick = { openDrawer() })
            })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                DeveloperOptions(
                   currentUser = currentUser,
                   databaseName = viewModel.databaseName.value,
                   logMessages = viewModel.logMessages,
                   onLoadSampleDataClick = viewModel::onLoadSampleData,
                   onDeleteDatabaseClick = viewModel::onDeleteDatabase)
            }
        }
    }
}

@Composable
fun DeveloperOptions(
    currentUser: UserProfile,
    databaseName: String,
    logMessages: List<String>,
    onLoadSampleDataClick: () -> Unit,
    onDeleteDatabaseClick: () -> Unit){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)){
        item {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth())
            {
                Text("Developer Options", style = MaterialTheme.typography.h4)
            }
        }
        item {
            Divider(
                color = Color.LightGray,
                thickness = 2.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp))
        }
        item {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp))
            {
                Text("Username: ${currentUser.username} ")
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp))
            {
                Text("Team: ${currentUser.team}")
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp))
            {
                Text("Database: ${databaseName}")
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp))
            {
                TextButton(onClick = { onLoadSampleDataClick() }) {
                    Text("Load Sample Data", style = MaterialTheme.typography.h6)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp))
            {
                TextButton(onClick = { onDeleteDatabaseClick() }) {
                    Text("Delete Database", style = MaterialTheme.typography.h6)
                }
            }
        }
        item {
            Divider(
                color = Color.LightGray,
                thickness = 2.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp))
        }
        logMessages.forEach {
            item {
                Text(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeveloperOptionsPreview(){
    InventoryTheme {
        DeveloperOptions(
            currentUser = UserProfile(),
            databaseName = "project-team1",
            logMessages = listOf(""),
            onLoadSampleDataClick =  { },
            onDeleteDatabaseClick = { })
    }
}