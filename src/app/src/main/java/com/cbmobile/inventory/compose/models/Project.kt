package com.cbmobile.inventory.compose.models

import java.text.SimpleDateFormat
import java.util.*

class ProjectModelDTO(override var item: Project) : ModelDTO<Project>

data class Project (
    var projectId: String = "",
    var name: String = "",
    var description: String = "",
    var isComplete: Boolean = false,
    var type: String = "project",
    var dueDate: Date? = null,
    var location: Location? = null,

    //security tracking
    var team: String = "",
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

            calendar.timeInMillis = it.time
            return formatter.format(calendar.time)
        }
        return ""
    }

}