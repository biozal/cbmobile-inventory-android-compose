package com.cbmobile.inventory.compose.data.replication

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.cbmobile.inventory.compose.data.DatabaseResource
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.couchbase.lite.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.net.URI

@InternalCoroutinesApi
@OptIn( ExperimentalCoroutinesApi::class)
class ReplicationServiceDb(val inventoryDatabase: InventoryDatabase) : ReplicationService {
    private var databaseResource: DatabaseResource? = null

    //track replication state
    override var isReplicationStarted = false

    override var replicationConfigDTO = mutableStateOf(ReplicationConfigDTO(
        username = "",
        password = "",
        endpointUrl = "ws://192.168.1.250:4984/projects",
        replicatorType = "PUSH AND PULL",
        heartBeat = 60L,
        continuous = true,
        selfSignedCert = true))

    override val replicationStatus = mutableStateOf("")
    override val replicationTypes = listOf("PUSH AND PULL", "PUSH", "PULL")
    override val canStartReplication = mutableStateOf(false)

    override fun updateReplicationConfig(replicationConfigDTO: ReplicationConfigDTO) {
        if ( databaseResource?.replicatorConfiguration == null ||
            (databaseResource?.replicator?.status?.activityLevel == ReplicatorActivityLevel.STOPPED ||
            databaseResource?.replicator?.status?.activityLevel == ReplicatorActivityLevel.IDLE ||
            databaseResource?.replicator?.status?.activityLevel == ReplicatorActivityLevel.OFFLINE)
        ){
            databaseResource?.let { dbResource ->
                val urlEndPoint = URLEndpoint(URI(replicationConfigDTO.endpointUrl))
                dbResource.replicatorConfiguration = ReplicatorConfiguration(dbResource.database, urlEndPoint)
                dbResource.replicatorConfiguration?.let { replicatorConfiguration ->
                    replicatorConfiguration.isContinuous = replicationConfigDTO.continuous

                    when (replicationConfigDTO.replicatorType) {
                        "PULL" -> replicatorConfiguration.type = ReplicatorType.PULL
                        "PUSH" -> replicatorConfiguration.type = ReplicatorType.PUSH
                        else -> replicatorConfiguration.type =  ReplicatorType.PUSH_AND_PULL
                    }
                    val authenticator = BasicAuthenticator(
                        replicationConfigDTO.username,
                        replicationConfigDTO.password.toCharArray()
                    )
                    replicatorConfiguration.setAuthenticator(authenticator)
                    dbResource.replicator =
                        Replicator(databaseResource?.replicatorConfiguration!!)
                }

                canStartReplication.value = true
                this.replicationConfigDTO.value = replicationConfigDTO
            }
        } else {
                throw Exception("Error: can't update Replicator Config because replication is running")
        }
    }

    override fun getReplicationConfig(): ReplicationConfigDTO {
        inventoryDatabase.loggedInUser?.username?.let { username ->
            inventoryDatabase.loggedInUser?.password?.let { password ->
                replicationConfigDTO.value.username = username
                replicationConfigDTO.value.password = password
                return replicationConfigDTO.value
            }
        }
        return replicationConfigDTO.value
    }

    override fun calculateReplicationStatus() {
       if (databaseResource == null){
           databaseResource = inventoryDatabase.databases[inventoryDatabase.projectDatabaseName]
           replicationStatus.value = "${ReplicationStatus.NOCONFIG}, use Cog toolbar icon to set config"
       } else if (databaseResource?.replicatorConfiguration == null) {
           replicationStatus.value = "${ReplicationStatus.NOCONFIG}, use Cog toolbar icon to set config"
       } else if (databaseResource?.replicator == null){
           replicationStatus.value = "${ReplicationStatus.UNINITIALIZED}, use Cog toolbar icon to set config and replicator.  THIS SHOULD NEVER BE THE STATE"
       } else {
           databaseResource?.replicator?.let {
               when(it.status.activityLevel){
                   ReplicatorActivityLevel.OFFLINE -> replicationStatus.value = ReplicationStatus.OFFLINE
                   ReplicatorActivityLevel.IDLE -> replicationStatus.value = ReplicationStatus.IDlE
                   ReplicatorActivityLevel.STOPPED -> replicationStatus.value = ReplicationStatus.STOPPED
                   ReplicatorActivityLevel.BUSY -> replicationStatus.value = ReplicationStatus.BUSY
                   ReplicatorActivityLevel.CONNECTING -> replicationStatus.value = ReplicationStatus.CONNECTING
               }
           }
       }
    }

    override fun startReplication() {
        try {
            databaseResource?.replicator?.start()
            isReplicationStarted = true
        } catch (e: Exception) {
            Log.e(e.message, e.stackTraceToString())
        }
    }

    override fun stopReplication() {
        try {
            databaseResource?.replicator?.stop()
            isReplicationStarted = false
        } catch (e: Exception) {
            Log.e(e.message, e.stackTraceToString())
        }
    }

    override fun getReplicatorChangeFlow(): Flow<ReplicatorChange>? {
        databaseResource?.replicator?.let {
            return it.replicatorChangesFlow()
        }
        return null
    }

    override fun dispose() {
    }
}


object ReplicationStatus {
    const val STOPPED = "Stopped"
    const val OFFLINE = "Offline"
    const val IDlE = "Idle"
    const val BUSY = "Busy"
    const val CONNECTING = "Connecting"
    const val UNINITIALIZED = "Not Initialized"
    const val NOCONFIG = "No Replication Configuration"
}