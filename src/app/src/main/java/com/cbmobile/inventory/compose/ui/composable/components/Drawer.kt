package com.cbmobile.inventory.compose.ui.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cbmobile.inventory.compose.ui.composable.MainDestinations
import com.cbmobile.inventory.compose.ui.composable.ui.theme.InventoryTheme

sealed class DrawerMenu(val title: String, val route: String, val imageVector: ImageVector){
    object Home: DrawerMenu("Home", MainDestinations.HOME_ROUTE, Icons.Filled.Home)
    object Replication: DrawerMenu("Replication", MainDestinations.REPLICATION_ROUTE, Icons.Filled.Sync)
    object Developer: DrawerMenu("Developer", MainDestinations.DEVELOPER_ROUTE, Icons.Filled.DeveloperMode)
    object Logout: DrawerMenu("Logout", MainDestinations.LOGOUT_ROUTE, Icons.Filled.Logout)
}

private val menu = listOf(
    DrawerMenu.Home,
    DrawerMenu.Replication,
    DrawerMenu.Developer,
    DrawerMenu.Logout)

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    onClicked: (route: String) -> Unit)
{
    Column(
        modifier
            .fillMaxSize()
    ){
        Box(modifier = Modifier
            .background(color = Color(0xFFbd000e))
            .fillMaxWidth()
            .height(80.dp)
        ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        text = "Inventory Demo",
                        style = MaterialTheme.typography.h5,
                        color = Color.White)
                }
        }
        Column(
            modifier
                .padding(start = 24.dp, top = 12.dp)
        )
        {
            menu.forEach { menuItem ->
                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClicked(menuItem.route) } ,
                    verticalAlignment = Alignment.CenterVertically )
                {
                    Icon(
                        menuItem.imageVector,
                        contentDescription = "",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = menuItem.title,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DrawerPreview() {
    val onClicked: (String) -> Unit = { _ -> }
    InventoryTheme {
        Drawer(onClicked = onClicked)
    }
}
