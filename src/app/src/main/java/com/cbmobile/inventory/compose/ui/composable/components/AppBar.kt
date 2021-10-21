package com.cbmobile.inventory.compose.ui.composable.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

import com.cbmobile.inventory.compose.ui.composable.ui.theme.InventoryTheme

@Composable
fun InventoryAppBar(title: String = "",
                    navigationIcon: ImageVector,
                    navigationOnClick: () -> Unit,
                    menuAction: (@Composable()() -> Unit)? = null)
{
    InventoryTheme {
        TopAppBar(
            backgroundColor = Color(0xFFbd000e),
            contentColor = Color.White,
            title = {
                Row {
                    Text(text = title)
                }
            },
            navigationIcon = {
                IconButton(onClick = { navigationOnClick() }){
                    Icon(navigationIcon, contentDescription="")
                }
            },
            actions = {
                menuAction?.let { icon ->
                    icon()
                }
            }
        )
    }
}

sealed class MenuAction(
    val label: String,
    val icon: ImageVector){
    object Settings: MenuAction("Settings", Icons.Filled.Settings)
    object Save: MenuAction("Save", Icons.Filled.Save)

}