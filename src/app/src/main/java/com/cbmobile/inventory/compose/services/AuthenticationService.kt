package com.cbmobile.inventory.compose.services

import com.cbmobile.inventory.compose.models.UserProfile

interface AuthenticationService {
    fun authenticatedUser(username: String, password: String) : Boolean
}