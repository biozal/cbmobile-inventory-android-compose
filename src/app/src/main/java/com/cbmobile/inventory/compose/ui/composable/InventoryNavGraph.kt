package com.cbmobile.inventory.compose.ui.composable

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cbmobile.inventory.compose.data.AppContainer
import com.cbmobile.inventory.compose.ui.composable.audit.AuditEditorScreen
import com.cbmobile.inventory.compose.ui.composable.audit.AuditListScreen
import com.cbmobile.inventory.compose.ui.composable.project.ProjectListScreen
import com.cbmobile.inventory.compose.ui.composable.project.ProjectEditorScreen
import kotlinx.coroutines.CoroutineScope

/**
 * Destinations used in the ([InventoryApp])
 */
object MainDestinations {
    const val HOME_ROUTE = "projects"
    const val PROJECT_EDITOR_ROUTE_PATH = "projectEditor/{project}"
    const val PROJECT_EDITOR_ROUTE = "projectEditor"
    const val AUDIT_LIST_ROUTE_PATH = "auditList/{project}"
    const val AUDIT_LIST_ROUTE = "auditList"
    const val AUDIT_EDITOR_ROUTE_PATH = "auditEditor/{audit}"
    const val AUDIT_EDITOR_ROUTE = "auditEditor"
    const val PROJECT_EDITOR_KEY_ID = "project"
    const val AUDIT_EDITOR_KEY_ID = "audit"
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
    snackBarCoroutineScope: CoroutineScope,
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
               actions.navigateToAuditListByProject,
               scaffoldState =  scaffoldState,
               snackBarCoroutineScope = snackBarCoroutineScope
           )
       }
       composable(MainDestinations.PROJECT_EDITOR_ROUTE_PATH){ backstackEntry ->
            ProjectEditorScreen(
                backstackEntry.arguments?.getString(MainDestinations.PROJECT_EDITOR_KEY_ID),
                appContainer.projectRepository,
                actions.upPress,
                scaffoldState = scaffoldState,
                lifecycleScope = lifecycleScope
            )
        }
        composable(MainDestinations.AUDIT_LIST_ROUTE_PATH){ backstackEntry ->
            AuditListScreen(
                backstackEntry.arguments?.getString(MainDestinations.PROJECT_EDITOR_KEY_ID),
                appContainer.auditRepository,
                appContainer.projectRepository,
                actions.upPress,
                actions.navigateToAuditEditor,
                lifecycleScope = lifecycleScope)
        }
        composable(MainDestinations.AUDIT_EDITOR_ROUTE_PATH){ backstackEntry ->
            AuditEditorScreen(
                backstackEntry.arguments?.getString(MainDestinations.AUDIT_EDITOR_KEY_ID ),
                appContainer.auditRepository,
                actions.upPress,
                lifecycleScope = lifecycleScope
            )
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val navigateToProjectEditor: (String) -> Unit = { projectJson: String ->
        navController.navigate("${MainDestinations.PROJECT_EDITOR_ROUTE}/$projectJson")
    }

    val navigateToAuditListByProject: (String) -> Unit = { projectJson: String ->
        navController.navigate("${MainDestinations.AUDIT_LIST_ROUTE}/$projectJson")
    }

    val navigateToAuditEditor:(String) -> Unit = { auditJson: String ->
       navController.navigate("${MainDestinations.AUDIT_EDITOR_ROUTE}/$auditJson")
    }

    val upPress: () -> Unit = {
        navController.popBackStack()
    }
}
