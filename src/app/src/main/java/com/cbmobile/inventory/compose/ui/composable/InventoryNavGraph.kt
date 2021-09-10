package com.cbmobile.inventory.compose.ui.composable

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cbmobile.inventory.compose.data.AppContainer
import com.cbmobile.inventory.compose.ui.composable.project.ProjectListScreen
import com.cbmobile.inventory.compose.ui.composable.project.ProjectEditorScreen
import kotlinx.coroutines.launch


/**
 * Destinations used in the ([InventoryApp])
 */
object MainDestinations {
    const val HOME_ROUTE = "projects"
    const val PROJECT_EDITOR_ROUTE_PATH = "projectEditor/{projectId}"
    const val PROJECT_EDITOR_ROUTE = "projectEditor"
    const val PROJECT_EDITOR_KEY_ID = "projectId"
    const val AUDIT_LIST_ROUTE = "auditList"
    const val AUDIT_EDITOR_ROUTE = "auditEditor"
    const val SYNC_MENU = "syncMenu"
    const val SYNC_SERVER_ROUTE = "syncServer"
    const val SYNC_SERVER_STATUS_ROUTE = "syncServerSTATUS"
    const val SYNC_PTP_ROUTE = "syncPTP"
    const val SYNC_PTP_STATUS_ROUTE = "syncPTPSTATUS"
}

@Composable
fun InventoryNavGraph(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    lifecycleScope: LifecycleCoroutineScope,
    startDestination: String = MainDestinations.HOME_ROUTE )
{

    val actions = remember(navController) { MainActions(navController) }
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = startDestination){
       composable(MainDestinations.HOME_ROUTE) {
           ProjectListScreen(
               appContainer.projectRepository,
               actions.navigateToProjectEditor,
               scaffoldState =  scaffoldState
           )
       }
       composable(MainDestinations.PROJECT_EDITOR_ROUTE_PATH){ backstackEntry ->
            ProjectEditorScreen(
                backstackEntry.arguments?.getString("projectId"),
                appContainer.projectRepository,
                actions.upPress,
                scaffoldState = scaffoldState,
                lifecycleScope = lifecycleScope
            )
        }
    }
}


/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val navigateToProjectEditor: (String) -> Unit = { projectId: String ->
        navController.navigate("${MainDestinations.PROJECT_EDITOR_ROUTE}/$projectId")
    }
    val navigateToAuditListByProject: (String) -> Unit = { projectId: String ->
        navController.navigate("${MainDestinations.AUDIT_LIST_ROUTE}/$projectId")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
