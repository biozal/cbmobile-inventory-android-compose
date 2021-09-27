package com.cbmobile.inventory.compose.ui.composable.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cbmobile.inventory.compose.models.Location

@Composable
fun LocationListSelection(
    selection: String,
    locations: List<Location>,
    onLocationChanged: (Location) -> Unit)
{
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.TopStart)
        .padding(top = 10.dp)
        .border(BorderStroke(.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f)))
        .clickable{
            expanded = true
        }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {

            val (label, iconView) = createRefs()

            Text(
                text = selection,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(label) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(iconView.start)
                        width = Dimension.fillToConstraints
                    }
            )
            Icon(
                imageVector = Icons.Default.AddLocation,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp, 20.dp)
                    .constrainAs(iconView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                tint = MaterialTheme.colors.onSurface
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false })
            {
                locations?.forEach { location ->
                    DropdownMenuItem(onClick = {
                        onLocationChanged(location)
                        expanded = false
                    }) {
                        Text(location.name)
                    }
                }
            }
        }
    }
}
