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
import com.cbmobile.inventory.compose.services.AuthenticationService

class LoginViewModel(private val authenticationService: AuthenticationService) : ViewModel() {
    private val _username = MutableLiveData<String>("")
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>("")
    val password: LiveData<String> = _password

    private val _isError = MutableLiveData<Boolean>(false)
    val isError: LiveData<Boolean> = _isError

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
        _username?.value?.let {  uname ->
            _password?.value?.let { pwd ->
                if (authenticationService.authenticatedUser(username = uname, password = pwd)) {
                    _isError.value = false
                    return true
                }
            }
        }
        _isError.value = true
        return false
    }
}