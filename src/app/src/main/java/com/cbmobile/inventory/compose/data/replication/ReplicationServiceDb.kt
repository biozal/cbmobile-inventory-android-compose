package com.cbmobile.inventory.compose.data.replication

import android.content.Context
import com.cbmobile.inventory.compose.data.InventoryDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@OptIn( ExperimentalCoroutinesApi::class)
class ReplicationServiceDb(var context: Context) : ReplicationService {
    private val databaseResources: InventoryDatabase = InventoryDatabase.getInstance(context)

}