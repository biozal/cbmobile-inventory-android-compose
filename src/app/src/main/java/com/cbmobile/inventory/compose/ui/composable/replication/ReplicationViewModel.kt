package com.cbmobile.inventory.compose.ui.composable.replication

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import com.couchbase.lite.ReplicatorActivityLevel
import com.cbmobile.inventory.compose.data.replication.ReplicationService
import com.cbmobile.inventory.compose.data.replication.ReplicationStatus

class ReplicationViewModel(private val replicationService: ReplicationService)
    : ViewModel()
{
    val logMessages = mutableStateListOf("")
    val replicationProgress = mutableStateOf("Not Started")
    val replicationStatus =  replicationService.replicationStatus
    val isButtonActive = replicationService.canStartReplication

    fun updateStatus() {
        replicationService.calculateReplicationStatus()
    }

    fun clearLogs() {
        logMessages.clear()
    }

    fun onStopClick() {
        viewModelScope.launch(Dispatchers.IO){
            logMessages.add("INFORMATION:: Stopping Replication...")
            replicationService.stopReplication()
            logMessages.add("INFORMATION:: Replication Service notified to stop...")
        }
    }

    fun onStartClick () {
        viewModelScope.launch(Dispatchers.IO) {
            when (replicationStatus.value) {
                ReplicationStatus.STOPPED -> {

                    logMessages.add("INFORMATION:: Starting Replication...")
                    replicationService.startReplication()

                    logMessages.add("INFORMATION:: Getting Replication Change Flow...")
                    val changeFlow = replicationService.getReplicatorChangeFlow()
                    changeFlow?.let { flowChange ->
                        flowChange.collect {  replicatorChange ->
                            logMessages.add("INFORMATION:: Collecting Replication Change Flow...")

                            when (replicatorChange.status.activityLevel){
                                ReplicatorActivityLevel.OFFLINE -> replicationStatus.value = ReplicationStatus.OFFLINE
                                ReplicatorActivityLevel.IDLE -> replicationStatus.value = ReplicationStatus.IDlE
                                ReplicatorActivityLevel.STOPPED -> replicationStatus.value = ReplicationStatus.STOPPED
                                ReplicatorActivityLevel.BUSY -> replicationStatus.value = ReplicationStatus.BUSY
                                ReplicatorActivityLevel.CONNECTING -> replicationStatus.value = ReplicationStatus.CONNECTING
                            }
                            replicatorChange.status.error?.let { error ->
                                logMessages.add("ERROR:: ${error.code} - ${error.message}")
                            }
                            logMessages.add("INFORMATION:: Checking replication progress...")
                            if (replicatorChange.status.progress.completed == replicatorChange.status.progress.total){
                                replicationProgress.value = "Completed"
                            } else {
                                replicationProgress.value = "${replicatorChange.status.progress.total / replicatorChange.status.progress.completed}"
                            }
                        }
                    }
                    logMessages.add("INFORMATION:: Replication Started method completed...")
                }
                else -> {
                    logMessages.add("INFORMATION:: Stopping replication...")
                    replicationService.stopReplication()
                }
            }
        }
    }
}