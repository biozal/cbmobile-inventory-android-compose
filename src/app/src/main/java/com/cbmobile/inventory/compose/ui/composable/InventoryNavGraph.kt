package com.cbmobile.inventory.compose.ui.composable

import android.content.Intent
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cbmobile.inventory.compose.AppContainer
import com.cbmobile.inventory.compose.models.UserProfile
import com.cbmobile.inventory.compose.ui.composable.developer.DeveloperScreen
import com.cbmobile.inventory.compose.ui.composable.replication.ReplicationScreen
import com.cbmobile.inventory.compose.ui.composable.audit.AuditEditorScreen
import com.cbmobile.inventory.compose.ui.composable.audit.AuditListScreen
import com.cbmobile.inventory.compose.ui.composable.login.LoginActivity
import com.cbmobile.inventory.compose.ui.composable.project.ProjectListScreen
import com.cbmobile.inventory.compose.ui.composable.project.ProjectEditorScreen
import com.cbmobile.inventory.compose.ui.composable.replication.ReplicationConfigScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi

/*
    Destinations used in routing
 */
object MainDestinations {
    const val HOME_ROUTE = "projects"
    const val REPLICATION_ROUTE = "replication"
    const val REPLICATION_SETTINGS_ROUTE = "replicationConfig"
    const val DEVELOPER_ROUTE = "developer"
    const val LOGOUT_ROUTE = "logout"
    const val PROJECT_EDITOR_ROUTE_PATH = "projectEditor/{project}"
    const val PROJECT_EDITOR_ROUTE = "projectEditor"
    const val AUDIT_LIST_ROUTE_PATH = "auditList/{project}"
    const val AUDIT_LIST_ROUTE = "auditList"
    const val AUDIT_EDITOR_ROUTE_PATH = "auditEditor/{project}/{audit}"
    const val AUDIT_EDITOR_ROUTE = "auditEditor"
    const val PROJECT_EDITOR_KEY_ID = "project"
    const val AUDIT_EDITOR_KEY_ID = "audit"
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun InventoryNavGraph(
    openDrawer: () -> Unit,
    currentUser: UserProfile,
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    scope: CoroutineScope,
    startDestination: String = MainDestinations.HOME_ROUTE )
{
    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination){
        composable(MainDestinations.HOME_ROUTE) {
            ProjectListScreen(
               viewModel = appContainer.projectListViewModel,
               openDrawer = openDrawer,
               navigateToProjectEditor =  actions.navigateToProjectEditor,
               navigateToAuditListByProject =  actions.navigateToAuditListByProject,
               scaffoldState =  scaffoldState,
               snackBarCoroutineScope = scope
           )
       }
       composable(MainDestinations.PROJECT_EDITOR_ROUTE_PATH){ backstackEntry ->
            ProjectEditorScreen(
                currentUser =  currentUser,
                projectJson = backstackEntry.arguments?.getString(MainDestinations.PROJECT_EDITOR_KEY_ID),
                projectRepository = appContainer.projectRepository,
                locationRepository = appContainer.locationRepository,
                navigateUp = actions.upPress,
                scaffoldState = scaffoldState
            )
        }
        composable(MainDestinations.AUDIT_LIST_ROUTE_PATH){ backstackEntry ->
            AuditListScreen(
                openDrawer = openDrawer,
                backstackEntry.arguments?.getString(MainDestinations.PROJECT_EDITOR_KEY_ID),
                appContainer.auditRepository,
                appContainer.projectRepository,
                actions.upPress,
                actions.navigateToAuditEditor)
        }
        composable(MainDestinations.AUDIT_EDITOR_ROUTE_PATH){ backstackEntry ->
            AuditEditorScreen(
                openDrawer = openDrawer,
                projectId = backstackEntry.arguments?.getString(MainDestinations.PROJECT_EDITOR_KEY_ID),
                auditJson = backstackEntry.arguments?.getString(MainDestinations.AUDIT_EDITOR_KEY_ID ),
                auditRepository = appContainer.auditRepository,
                navigateUp = actions.upPress)
        }
        composable(MainDestinations.DEVELOPER_ROUTE){
            DeveloperScreen(
                viewModel = appContainer.developerViewModel,
                openDrawer = openDrawer,
                currentUser =  currentUser,
                scaffoldState = scaffoldState,
            )
        }
        composable(MainDestinations.REPLICATION_ROUTE){
            ReplicationScreen(
                viewModel = appContainer.replicationViewModel,
                openDrawer = openDrawer,
                replicationConfigNav = actions.navigateToReplicationConfig,
                scaffoldState = scaffoldState
            )
        }
        composable(MainDestinations.REPLICATION_SETTINGS_ROUTE){
            ReplicationConfigScreen(
                viewModel = appContainer.replicationConfigViewModel,
                navigateUp = actions.upPress,
                scaffoldState = scaffoldState
            )
        }
        composable(MainDestinations.LOGOUT_ROUTE){
            val context = LocalContext.current
            context.startActivity(Intent(context, LoginActivity::class.java))
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

    val navigateToAuditEditor:(String, String) -> Unit = { project: String, audit: String ->
       navController.navigate("${MainDestinations.AUDIT_EDITOR_ROUTE}/$project/$audit")
    }

    val navigateToReplicationConfig: () -> Unit = {
        navController.navigate(MainDestinations.REPLICATION_SETTINGS_ROUTE)
    }

    val upPress: () -> Unit = {
        navController.popBackStack()
    }


}
