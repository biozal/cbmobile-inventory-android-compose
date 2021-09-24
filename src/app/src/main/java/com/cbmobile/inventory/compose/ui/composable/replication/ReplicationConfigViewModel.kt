package com.cbmobile.inventory.compose.ui.composable.replication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ReplicationConfigViewModel()
    : ViewModel() {

    val username: String = "demo@example.com"
    val password: String = "password"
    val continuousState =  mutableStateOf(true)
    val selfSignedCertState =  mutableStateOf(true)
    val replicatorTypes = listOf("PUSH AND PULL", "PUSH", "PULL")
    val replicatorTypeSelected =  mutableStateOf("PUSH AND PULL")
    val serverUrlState = mutableStateOf("wss://localhost:4984/projects")
    val heartBeatState = mutableStateOf(60L)

    val onContinuousChanged: (Boolean) -> Unit =  { newValue ->
        continuousState.value = newValue
    }

    val onSelfSignedCertChanged: (Boolean) -> Unit = { newValue ->
        selfSignedCertState.value = newValue
    }

    val onReplicatorTypeChanged: (String) -> Unit = { newValue ->
        replicatorTypeSelected.value = newValue
    }

    val onServerUrlChanged: (String) -> Unit = { newValue ->
        serverUrlState.value = newValue
    }

    val onHeartBeatChanged: (Long) -> Unit = { newValue ->
        heartBeatState.value = newValue
    }

    fun Save() {

    }
}