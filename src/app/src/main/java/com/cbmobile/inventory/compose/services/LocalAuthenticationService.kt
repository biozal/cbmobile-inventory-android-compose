package com.cbmobile.inventory.compose.services

import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.models.UserProfile

class LocalAuthenticationService(val inventoryDatabase: InventoryDatabase) : AuthenticationService {
    private val users = mutableListOf<UserProfile>()

    init {
        users.add(UserProfile(
            username = "demo@example.com",
            password = "password",
            givenName = "Dmitry",
            surname = "Jemerov",
            team = "team1",
            isActive = true))
        users.add(UserProfile(
            username = "demo1@example.com",
            password = "password",
            givenName = "Andrey",
            surname = "Breslav",
            team = "team1",
            isActive = true))
        users.add(UserProfile(
            username = "demo2@example.com",
            password = "password",
            givenName = "Barbara",
            surname = "Liskov",
            team = "team2",
            isActive = true))
        users.add(UserProfile(
            username = "demo3@example.com",
            password = "password",
            givenName = "Larry",
            surname = "Page",
            team = "team2",
            isActive = true))
        users.add(UserProfile(
            username = "demo4@example.com",
            password = "password",
            givenName = "Denis",
            surname = "Rosa",
            team = "team3",
            isActive = true))
        users.add(UserProfile(
            username = "demo5@example.com",
            password = "password",
            givenName = "Steve",
            surname = "Yen",
            team = "team3",
            isActive = true))
    }

    override fun authenticatedUser(username: String, password: String): Boolean {
        val filterUsers = users.filter { it.username == username && it.password == password && it.isActive }
        if (filterUsers.count() == 1){
            inventoryDatabase.loggedInUser = filterUsers[0]
            return true
        }
        return false
    }
}