package com.cbmobile.inventory.compose.ui.composable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cbmobile.inventory.compose.InventoryApplication
import com.cbmobile.inventory.compose.ui.composable.ui.theme.InventoryTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as InventoryApplication).container

        setContent {
            ProvideWindowInsets {
                val systemUiController = rememberSystemUiController()

                /*
                SideEffect {
                    systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
                }
                */

                val navController = rememberNavController()
                val corouteScope = rememberCoroutineScope()

                //we need a drawer overflow menu on multiple screens
                //so we need top level scaffold.  An event to open the drawer is passed
                //to each screen that needs it.
                val scaffoldState = rememberScaffoldState()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE
                InventoryTheme {
                    Scaffold(scaffoldState = scaffoldState,
                        topBar = { InventoryAppBar() }) {
                        InventoryNavGraph(
                            appContainer = appContainer,
                            navController = navController,
                            scaffoldState = scaffoldState
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryAppBar() {
    InventoryTheme {
        TopAppBar(
            backgroundColor = Color(0xFFbd000e),
            title = {
                Row {
                    Text("Inventory")
                }
            },
        )
    }
}