package com.cbmobile.inventory.compose.ui.composable.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cbmobile.inventory.compose.ui.composable.ui.theme.InventoryTheme

@Composable
fun InventoryAppBar(title: String) {
    InventoryTheme {
        TopAppBar(
            backgroundColor = Color(0xFFbd000e),
            contentColor = Color.White,
            title = {
                Row {
                    Text(text = title)
                }
            },
        )
    }
}