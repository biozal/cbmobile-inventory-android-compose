package com.cbmobile.inventory.compose.ui.composable.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.cbmobile.inventory.compose.ui.composable.ui.theme.InventoryTheme

@Composable
fun InventoryAppBar(title: String = "",
                    buttonIcon: ImageVector,
                    onClicked: () -> Unit)
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
                IconButton(onClick = { onClicked() }){
                    Icon(buttonIcon, contentDescription="")
                }
            }
        )
    }
}
