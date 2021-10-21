package com.cbmobile.inventory.compose.ui.composable.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbmobile.inventory.compose.services.AuthenticationService

class LoginViewModel(private val authenticationService: AuthenticationService) : ViewModel() {
    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _isError = MutableLiveData(false)
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
        _username.value?.let {  uname ->
            _password.value?.let { pwd ->
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