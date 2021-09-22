package com.cbmobile.inventory.compose.models

data class UserProfile(
    var username: String = "",
    var password: String = "",
    var givenName: String = "",
    var surname: String = "",
    var isActive: Boolean = true)
