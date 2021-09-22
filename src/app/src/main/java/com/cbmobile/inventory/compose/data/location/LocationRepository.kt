package com.cbmobile.inventory.compose.data.location

import com.cbmobile.inventory.compose.models.Location

interface LocationRepository {
    suspend fun getLocations(): List<Location>

}