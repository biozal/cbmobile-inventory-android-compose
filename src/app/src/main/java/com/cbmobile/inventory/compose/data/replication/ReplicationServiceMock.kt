package com.cbmobile.inventory.compose.data.replication

import androidx.compose.runtime.mutableStateOf
import com.couchbase.lite.ReplicatorChange
import kotlinx.coroutines.flow.Flow

class ReplicationServiceMock : ReplicationService {

    override var isReplicationStarted = false;

    override val replicationConfigDTO = mutableStateOf(ReplicationConfigDTO(
            username = "demo@example.com",
            password = "password",
            endpointUrl = "wss://localhost:4984/projects",
            replicatorType = "PUSH AND PULL",
            heartBeat = 60L,
            continuous = true,
            selfSignedCert = true))

    override val replicationStatus = mutableStateOf("No Replicator Set, use Cog toolbar icon to set config and replicator.")
    override val replicationTypes: List<String> = listOf("PUSH AND PULL", "PUSH", "PULL")
    override val canStartReplication = mutableStateOf(false)

    override fun updateReplicationConfig(replicationConfigDTO: ReplicationConfigDTO) {
        TODO("Not yet implemented")
    }

    override fun getReplicationConfig(): ReplicationConfigDTO {
        TODO("Not yet implemented")
    }

    override fun calculateReplicationStatus() {
        TODO("Not yet implemented")
    }

    override fun startReplication() {
        TODO("Not yet implemented")
    }

    override fun stopReplication() {
        TODO("Not yet implemented")
    }

    override fun getReplicatorChangeFlow(): Flow<ReplicatorChange>? {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}