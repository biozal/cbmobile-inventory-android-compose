package com.cbmobile.inventory.compose.ui.composable.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.models.UserProfile

class LoginViewModel(val context: Context) : ViewModel() {
    // used for sync gateway demo - hard coded username and passwords should never be used in real apps
    // this is for demo purposes only
    private val users = mutableListOf<UserProfile>()

    private val _username = MutableLiveData<String>("")
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>("")
    val password: LiveData<String> = _password

    private val _isError = MutableLiveData<Boolean>(false)
    val isError: LiveData<Boolean> = _isError

    init {
        // used for sync gateway demo - hard coded username and passwords should never be used in real apps
        users.add(UserProfile(username = "demo@example.com", password = "password", givenName = "Dmitry", surname = "Jemerov", isActive = true))
        users.add(UserProfile(username = "demo1@example.com", password = "password", givenName = "Andrey", surname = "Breslav", isActive = true))
        users.add(UserProfile(username = "demo2@example.com", password = "password", givenName = "Barbara", surname = "Liskov", isActive = true))
        users.add(UserProfile(username = "demo3@example.com", password = "password", givenName = "Larry", surname = "Page", isActive = true))
        users.add(UserProfile(username = "demo4@example.com", password = "password", givenName = "Denis", surname = "Rosa", isActive = true))
    }

    val onUsernameChanged: (String) -> Unit = { newValue ->
        _isError.value = false
        _username.value = newValue
    }

    val onPasswordChanged: (String) -> Unit = { newValue ->
        _isError.value = false
        _password.value = newValue
    }

    fun login () : Boolean
    {
        var filterUsers = users.filter { it.username == username.value && it.password == password.value }
        if (filterUsers.count() == 1){
            InventoryDatabase.getInstance(context).loggedInUser = filterUsers[0]
            _isError.value = false
            return true
        }
        _isError.value = true
        return false
    }
}