package com.cbmobile.inventory.compose.data.replication

data class ReplicationConfigDTO(
    var username: String,
    var password: String,
    var endpointUrl: String,
    var replicatorType: String,
    var heartBeat: Long,
    var continuous: Boolean,
    var selfSignedCert: Boolean)
