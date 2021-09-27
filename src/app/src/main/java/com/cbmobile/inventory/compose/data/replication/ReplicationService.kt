package com.cbmobile.inventory.compose.data.replication

import androidx.compose.runtime.MutableState
import com.couchbase.lite.ReplicatorChange
import kotlinx.coroutines.flow.Flow

interface ReplicationService {

    val replicationConfigDTO: MutableState<ReplicationConfigDTO>
    val replicationStatus: MutableState<String>

    val replicationTypes: List<String>
    val canStartReplication: MutableState<Boolean>

    fun updateReplicationConfig(replicationConfigDTO: ReplicationConfigDTO)
    fun getReplicationConfig() : ReplicationConfigDTO
    fun calculateReplicationStatus()

    fun startReplication()
    fun stopReplication()
    fun getReplicatorChangeFlow() : Flow<ReplicatorChange>?
}