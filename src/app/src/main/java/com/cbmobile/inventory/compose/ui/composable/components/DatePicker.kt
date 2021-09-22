package com.cbmobile.inventory.compose.ui.composable.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DatePicker(
    selectedDate: String,
    onDateChanged:(Long?) -> Unit){
    val activity = LocalContext.current

    Box(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
            .padding(top = 10.dp)
            .border(BorderStroke(.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f)))
            .clickable{
                showDatePickerDialog(activity, onDateChanged)
            }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {

            val (label, iconView) = createRefs()

            Text(
                text = selectedDate,
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
                imageVector = Icons.Default.DateRange,
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
        }
    }
}

fun showDatePickerDialog(activity: Context,
                         onDateChanged:(Long?) -> Unit) {
    val now = Calendar.getInstance()
    now.time = Date()
    val year: Int = now.get(Calendar.YEAR)
    val month: Int = now.get(Calendar.MONTH)
    val day: Int = now.get(Calendar.DAY_OF_MONTH)
    val startDate = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        activity,
        android.R.style.Theme_Material_Dialog,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            onDateChanged(cal.timeInMillis)
        }, year, month, day)

    startDate.set(Calendar.DAY_OF_MONTH, 1)
    datePickerDialog.datePicker.minDate = startDate.timeInMillis
    //datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
    datePickerDialog.show()
}

fun getFormattedDate(date: Date?, format: String): String {
    date?.let { formattedDate ->
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(formattedDate)
    }
    return ""
}