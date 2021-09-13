package com.cbmobile.inventory.compose.ui.composable.components

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import java.util.*

@Composable
fun AddButton(onClick: (String) -> Unit) {
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = FloatingActionButtonDefaults.elevation(),
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
        onClick = {
            onClick(UUID.randomUUID().toString())
        })
    {
        Icon(
            Icons.Default.Add,
            contentDescription = "add"
        )
    }
}
