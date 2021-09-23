package com.cbmobile.inventory.compose.ui.composable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cbmobile.inventory.compose.InventoryApplication
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.ui.composable.components.Drawer
import com.cbmobile.inventory.compose.ui.composable.ui.theme.InventoryTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as InventoryApplication).container

        setContent {
            ProvideWindowInsets {
                val systemUiController = rememberSystemUiController()

                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val inventoryDatabase = InventoryDatabase.getInstance(this.applicationContext)
                val currentUser = inventoryDatabase.loggedInUser!!
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE

                /*
                SideEffect {
                    systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
                }
                */

                //we need a drawer overflow menu on multiple screens
                //so we need top level scaffold.  An event to open the drawer is passed
                //to each screen that needs it.
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }

                InventoryTheme {
                    Scaffold(scaffoldState = scaffoldState,
                        snackbarHost = {
                            scaffoldState.snackbarHostState
                        }) {
                        ModalDrawer(
                            drawerState = drawerState,
                            gesturesEnabled = drawerState.isOpen,
                            drawerContent = {
                                Drawer(
                                    onClicked = { route ->
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                        ) {
                            InventoryNavGraph(
                                openDrawer =  { openDrawer() },
                                inventoryDatabase = inventoryDatabase,
                                currentUser = currentUser,
                                appContainer = appContainer,
                                navController = navController,
                                scaffoldState = scaffoldState,
                                lifecycleScope = lifecycleScope,
                                scope = scope
                            )
                        }
                    }
                }
            }
        }
    }
}