package com.cbmobile.inventory.compose.models
import androidx.annotation.Keep
import java.util.*

data class LocationDTO(
    val location: Location)

data class Location (
    val locationId: String,
    val name: String,
    val address1: String,
    val address2: String,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String,
    val latitude: Double,
    val longitude: Double,
    val type: String
)