package com.cbmobile.inventory.compose.ui.composable.replication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmobile.inventory.compose.data.replication.ReplicationConfigDTO
import com.cbmobile.inventory.compose.data.replication.ReplicationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReplicationConfigViewModel(private val replicationService:ReplicationService)
    : ViewModel() {

    private val replicationConfigDTO = replicationService.getReplicationConfig()

    val username: String = replicationConfigDTO.username
    val password: String = replicationConfigDTO.password
    val continuousState =  mutableStateOf(replicationConfigDTO.continuous)
    val selfSignedCertState =  mutableStateOf(replicationConfigDTO.selfSignedCert)
    val replicatorTypes = replicationService.replicationTypes
    val replicatorTypeSelected =  mutableStateOf(replicationConfigDTO.replicatorType)
    val serverUrlState = mutableStateOf(replicationConfigDTO.endpointUrl)
    val heartBeatState = mutableStateOf(replicationConfigDTO.heartBeat)

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

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            val dto = ReplicationConfigDTO(
                username = username,
                password = password,
                endpointUrl = serverUrlState.value,
                replicatorType = replicatorTypeSelected.value,
                heartBeat = heartBeatState.value,
                continuous = continuousState.value,
                selfSignedCert = selfSignedCertState.value
            )
            try {
                replicationService.updateReplicationConfig(dto)
            } catch (e: Exception){
                //todo throw update the UI with error that states replication is running can't perform this until it's stopped
            }
        }
    }
}