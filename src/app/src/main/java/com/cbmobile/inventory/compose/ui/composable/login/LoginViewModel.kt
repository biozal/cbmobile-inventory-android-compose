package com.cbmobile.inventory.compose.ui.composable.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    fun onUsernameChanged(newValue: String){
        username.value = newValue
    }

    fun onPasswordChanged(newValue: String){
        password.value = newValue
    }

    fun login() :Boolean {
        return true
    }
}