package com.cbmobile.inventory.compose.data

import android.content.Context
import com.cbmobile.inventory.compose.models.Wrapper
import com.couchbase.lite.Database
import com.couchbase.lite.MutableDocument
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class RepositoryBase(private val database: Database) : Repository {

    private suspend inline fun <reified W: Wrapper<T>, T: Any> getItemList(
        n1qlQuery: String,
        database: Database) : ArrayList<T>
    {
        val items = ArrayList<T>()
        return withContext(Dispatchers.IO) {
            try {
                val query = database.createQuery(n1qlQuery)
                query.execute().forEach() { item ->
                    val json = item.toJSON()
                    val wrapper = Gson().fromJson(json, W::class.java)
                    items.add(wrapper.item)
                }
            } catch (e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext items
        }
    }

    override suspend fun <W: Wrapper<T>, T: Any> getList(n1qlQuery: String) : List<T> {
        var items = ArrayList<T>()
        return withContext(Dispatchers.IO) {
            items = getItemList(n1qlQuery, database)
            return@withContext items
        }
    }

    override suspend fun <T: Any>getItem(key:String,
                                         method: () -> T): T?{
        return withContext(Dispatchers.IO) {
            try {
                val doc = database.getDocument(key)
                doc?.let { document ->
                        return@withContext Gson().fromJson(document.toJSON(), method()::class.java)
                }
            } catch (e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext null
        }
    }

    override suspend fun <T> save(key: String, item: T) {
        return withContext(Dispatchers.IO) {
            try {
                val json = Gson().toJson(item)
                when (val doc = database.getDocument(key)){
                    null -> {
                        val mutDoc = MutableDocument(key, json)
                        database.save(mutDoc)
                    }
                    else -> doc.let { document ->
                        val mutDoc = document.toMutable()
                        mutDoc.setJSON(json)
                        database.save(mutDoc)
                    }
                }
            } catch (e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
       }
    }

    override suspend fun delete(keyId: String) {
        return withContext(Dispatchers.IO){
            try {
                val doc = database.getDocument(keyId)
                doc?.let { document ->
                    database.delete(document)
                }
            } catch (e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
        }
    }
}