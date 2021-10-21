package com.cbmobile.inventory.compose.services

interface AuthenticationService {
    fun authenticatedUser(username: String, password: String) : Boolean
}