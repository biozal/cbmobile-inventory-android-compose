package com.cbmobile.inventory.compose.ui.composable.project

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.sourceInformationMarkerEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.location.LocationRepository
import com.cbmobile.inventory.compose.data.projects.ProjectRepository
import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.Project
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProjectViewModel(private val projectJson: String?,
                       private val projectRepository: ProjectRepository,
                       private val locationRepository: LocationRepository)
    : ViewModel() {
    var projectState = mutableStateOf(Project())
    val locationsState = mutableListOf<Location>()
    val dueDateState = mutableStateOf("Select Due Date")
    val locationSelectionState = mutableStateOf("Select Location")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val results = locationRepository.getLocations()
            for (result in results) {
                locationsState.add(result)
            }
            if (projectJson == null || projectJson == "create") {
                projectState.value = projectRepository.getProject(UUID.randomUUID().toString())
            } else {
                projectState.value = Gson().fromJson(projectJson, Project::class.java)
                projectState.value.location?.let { location ->
                    locationSelectionState.value = location.name
                }
                projectState.value.dueDate?.let { dueDate ->
                    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                    dueDateState.value = formatter.format(dueDate)
                }
            }
        }
    }

    private fun dateFormatter(milliseconds : Long) : String {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        return formatter.format(calendar.time)
    }

    val onDateChanged: (Long?) -> Unit =  { date ->
        date?.let { theDate ->
            dueDateState.value = dateFormatter(theDate)
            projectState.value.dueDate = Date(theDate)
        }
    }

    val onNameChanged: (String) -> Unit = { newValue ->
        val p = projectState.value.copy()
        p.name = newValue
        projectState.value = p
    }

    val onDescriptionChanged: (String) -> Unit = { newValue ->
        val p = projectState.value.copy()
        p.description = newValue
        projectState.value = p
    }

    val onLocationChanged: (Location) -> Unit = { newValue ->
        val p = projectState.value.copy()
        p.location = newValue
        projectState.value = p
        locationSelectionState.value = newValue.name
    }

    val onSaveProject: () -> Unit = {
        viewModelScope.launch(Dispatchers.IO){
            projectState.value.name = projectState.value.name.trim()
            projectState.value.description = projectState.value.description.trim()
            projectRepository.saveProject(projectState.value)
       }
    }
}