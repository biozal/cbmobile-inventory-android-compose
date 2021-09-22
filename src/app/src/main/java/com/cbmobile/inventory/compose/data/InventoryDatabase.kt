package com.cbmobile.inventory.compose.data

import android.content.Context
import com.cbmobile.inventory.compose.util.Singleton
import com.couchbase.lite.*

class InventoryDatabase private constructor(context: Context) {

    var databases: MutableMap<String, DatabaseResource> = mutableMapOf<String, DatabaseResource>()
    var projectDatabaseName = "project"
    var locationDatabase = "locations"

    init {
        //setup couchbase lite
        CouchbaseLite.init(context)
        Database.log.console.domains = LogDomain.ALL_DOMAINS
        Database.log.console.level = LogLevel.VERBOSE
    }

    companion object : Singleton<InventoryDatabase, Context>(::InventoryDatabase);
}