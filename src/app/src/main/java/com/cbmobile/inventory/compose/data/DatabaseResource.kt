package com.cbmobile.inventory.compose.data

import com.couchbase.lite.*

class DatabaseResource (var database: Database, var config: DatabaseConfiguration) {

    //replication
    var replicator: Replicator? = null
    var replicatorConfiguration: ReplicatorConfiguration? = null
    var replicatorChangeListenerToken: ListenerToken? = null

}