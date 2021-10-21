package com.cbmobile.inventory.compose.ui.composable.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoItemsFound () {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
       Column(modifier = Modifier.align(Alignment.Center)){
           Text(
               modifier = Modifier
                   .padding(8.dp)
                   .align(Alignment.CenterHorizontally),
               text = "",
               style = MaterialTheme.typography.h4
           )
       }
    }
}