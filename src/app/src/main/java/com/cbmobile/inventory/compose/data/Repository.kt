package com.cbmobile.inventory.compose.data

import com.cbmobile.inventory.compose.models.Wrapper

interface Repository {
    suspend fun <W: Wrapper<T>, T: Any> getList(n1qlQuery: String) : List<T>
    suspend fun <T: Any> getItem(key:String, method: () -> T): T?
    suspend fun <T> save(key: String, item: T)
    suspend fun delete(keyId: String)
}