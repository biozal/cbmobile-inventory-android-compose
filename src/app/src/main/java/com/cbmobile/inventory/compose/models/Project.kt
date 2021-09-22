package com.cbmobile.inventory.compose.models

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.toDuration

class ProjectDTO(override var item: Project) : DTO<Project> {
}

data class Project (
    var projectId: String = "",
    var name: String = "",
    var description: String = "",
    var isComplete: Boolean = false,
    var type: String = "project",
    var dueDate: Date? = null,
    var location: Location? = null,

    //security tracking
    var createdBy: String = "",
    var modifiedBy: String = "",
    var createdOn: Date? = null,
    var modifiedOn: Date? = null
){

    fun isOverDue(): Boolean {
        if (Date() > dueDate)
            return true
        return false
    }

    fun getDueDateString():String {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val calendar: Calendar = Calendar.getInstance()
        dueDate?.let {

            calendar.timeInMillis = it.time.toLong()
            return formatter.format(calendar.time)
        }
        return ""
    }

}