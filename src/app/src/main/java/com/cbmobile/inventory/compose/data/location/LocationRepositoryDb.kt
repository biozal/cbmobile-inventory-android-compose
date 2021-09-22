package com.cbmobile.inventory.compose.data.location

import android.content.Context
import android.util.Log
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.LocationDTO
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepositoryDb(context: Context) : LocationRepository {
    private val databaseResources: InventoryDatabase = InventoryDatabase.getInstance(context)

    override suspend fun getLocations(): List<Location> {
        return withContext(Dispatchers.IO) {
            val locationResults = mutableListOf<Location>()
            try {
                val db =
                    databaseResources.databases[databaseResources.projectDatabaseName]?.database
                db?.let { database ->
                    val query = database.createQuery("SELECT * FROM project AS location WHERE type = \"location\"")
                    query.execute().forEach { location ->
                        val json = location.toJSON()
                        val locationWrapper = Gson().fromJson(json, LocationDTO::class.java)
                        locationResults.add(locationWrapper.location)
                    }
                }
            } catch (e: Exception) {
                Log.e(e.message, e.stackTraceToString())
            }
            return@withContext locationResults
        }
    }
}