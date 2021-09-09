package com.cbmobile.inventory.compose.data

import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.couchbase.lite.ListenerToken
import com.couchbase.lite.Replicator

class DatabaseResource (var database: Database, var config: DatabaseConfiguration) {
    //database
    var databaseListenerToken: ListenerToken? = null

    //document
    var documentChangeListenerToken: ListenerToken? = null

    //replication
    var replicator: Replicator? = null
    var replicatorChangeListenerToken: ListenerToken? = null

}