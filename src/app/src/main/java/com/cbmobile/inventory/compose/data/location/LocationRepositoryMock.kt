package com.cbmobile.inventory.compose.data.location

import com.cbmobile.inventory.compose.models.Location

class LocationRepositoryMock : LocationRepository {

    override suspend fun getLocations(): List<Location> {
        TODO("Not yet implemented")
    }

}