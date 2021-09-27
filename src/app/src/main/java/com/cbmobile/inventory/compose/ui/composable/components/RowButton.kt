package com.cbmobile.inventory.compose.ui.composable.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RowButton(onClick: () -> Unit,
              displayText: String)
{
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(modifier = Modifier
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onClick = {
                    onClick()
                })
        {
            Text(displayText, style = MaterialTheme.typography.h5)
        }
    }
}