package com.cbmobile.inventory.compose.ui.composable

import android.content.Intent
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cbmobile.inventory.compose.data.AppContainer
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.models.UserProfile
import com.cbmobile.inventory.compose.ui.composable.Developer.DeveloperScreen
import com.cbmobile.inventory.compose.ui.composable.Replication.ReplicationScreen
import com.cbmobile.inventory.compose.ui.composable.audit.AuditEditorScreen
import com.cbmobile.inventory.compose.ui.composable.audit.AuditListScreen
import com.cbmobile.inventory.compose.ui.composable.login.LoginActivity
import com.cbmobile.inventory.compose.ui.composable.project.ProjectListScreen
import com.cbmobile.inventory.compose.ui.composable.project.ProjectEditorScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Destinations used in the ([InventoryApp])
 */
object MainDestinations {
    const val HOME_ROUTE = "projects"
    const val REPLICATION_ROUTE = "replication"
    const val DEVELOPER_ROUTE = "developer"
    const val LOGOUT_ROUTE = "logout"
    const val PROJECT_EDITOR_ROUTE_PATH = "projectEditor/{project}"
    const val PROJECT_EDITOR_ROUTE = "projectEditor"
    const val AUDIT_LIST_ROUTE_PATH = "auditList/{project}"
    const val AUDIT_LIST_ROUTE = "auditList"
    const val AUDIT_EDITOR_ROUTE_PATH = "auditEditor/{audit}"
    const val AUDIT_EDITOR_ROUTE = "auditEditor"
    const val PROJECT_EDITOR_KEY_ID = "project"
    const val AUDIT_EDITOR_KEY_ID = "audit"
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun InventoryNavGraph(
    openDrawer: () -> Unit,
    inventoryDatabase: InventoryDatabase,
    currentUser: UserProfile,
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    lifecycleScope: LifecycleCoroutineScope,
    scope: CoroutineScope,
    startDestination: String = MainDestinations.HOME_ROUTE )
{

    val actions = remember(navController) { MainActions(navController) }
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = startDestination){
       composable(MainDestinations.HOME_ROUTE) {
           ProjectListScreen(
               openDrawer = openDrawer,
               projectRepository =  appContainer.projectRepository,
               navigateToProjectEditor =  actions.navigateToProjectEditor,
               navigateToAuditListByProject =  actions.navigateToAuditListByProject,
               scaffoldState =  scaffoldState,
               snackBarCoroutineScope = scope
           )
       }
       composable(MainDestinations.PROJECT_EDITOR_ROUTE_PATH){ backstackEntry ->
            ProjectEditorScreen(
                openDrawer = openDrawer,
                currentUser =  currentUser,
                projectJson = backstackEntry.arguments?.getString(MainDestinations.PROJECT_EDITOR_KEY_ID),
                projectRepository = appContainer.projectRepository,
                locationRepository = appContainer.locationRepository,
                navigateUp = actions.upPress,
                scaffoldState = scaffoldState,
                lifecycleScope = lifecycleScope
            )
        }
        composable(MainDestinations.AUDIT_LIST_ROUTE_PATH){ backstackEntry ->
            AuditListScreen(
                openDrawer = openDrawer,
                backstackEntry.arguments?.getString(MainDestinations.PROJECT_EDITOR_KEY_ID),
                appContainer.auditRepository,
                appContainer.projectRepository,
                actions.upPress,
                actions.navigateToAuditEditor,
                lifecycleScope = lifecycleScope)
        }
        composable(MainDestinations.AUDIT_EDITOR_ROUTE_PATH){ backstackEntry ->
            AuditEditorScreen(
                openDrawer = openDrawer,
                backstackEntry.arguments?.getString(MainDestinations.AUDIT_EDITOR_KEY_ID ),
                appContainer.auditRepository,
                actions.upPress,
                lifecycleScope = lifecycleScope
            )
        }
        composable(MainDestinations.DEVELOPER_ROUTE){
            DeveloperScreen(
                openDrawer = openDrawer,
                currentUser =  currentUser,
                scaffoldState = scaffoldState,
                lifecycleScope = lifecycleScope
            )
        }
        composable(MainDestinations.REPLICATION_ROUTE){
            ReplicationScreen(
                openDrawer = openDrawer,
                scaffoldState = scaffoldState,
                lifecycleScope = lifecycleScope
            )
        }
        composable(MainDestinations.LOGOUT_ROUTE){
            val context = LocalContext.current
            inventoryDatabase.loggedInUser = null
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

    val navigateToAuditEditor:(String) -> Unit = { auditJson: String ->
       navController.navigate("${MainDestinations.AUDIT_EDITOR_ROUTE}/$auditJson")
    }

    val upPress: () -> Unit = {
        navController.popBackStack()
    }
}
