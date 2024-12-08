package com.snofed.publicapp.viewModel

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.realm.Case

open class GenericViewModel<T : RealmObject>(private var realmClass: Class<T>) : ViewModel() {

    private val realm: Realm = Realm.getDefaultInstance()
    private val gson = Gson()

    // Save or update an object
    fun saveOrUpdate(item: T) {
        realm.executeTransaction { transactionRealm ->
            transactionRealm.insertOrUpdate(item)
        }
    }

    // Save or update a list of objects
    fun saveOrUpdateAll(items: List<T>) {
        realm.executeTransaction { transactionRealm ->
            transactionRealm.insertOrUpdate(items)
        }
    }

    // Fetch all items as a list
    fun fetchAll(): List<T> {
        val results = realm.where(realmClass).findAll()
        return realm.copyFromRealm(results)
    }

    // Fetch an item by ID
    fun fetchById(id: String): T? {
        return realm.where(realmClass).equalTo("id", id, Case.INSENSITIVE).findFirst()?.let {
            realm.copyFromRealm(it)
        }
    }

    // Delete an item by ID
    fun deleteById(id: String) {
        realm.executeTransaction { transactionRealm ->
            val item = transactionRealm.where(realmClass).equalTo("id", id).findFirst()
            item?.deleteFromRealm()
        }
    }

    // Delete all items
    fun deleteAll() {
        realm.executeTransaction { transactionRealm ->
            val results = transactionRealm.where(realmClass).findAll()
            results.deleteAllFromRealm()
        }
    }

    // Count the total number of items
    fun count(): Long {
        return realm.where(realmClass).count()
    }

    // Query items with a custom filter
    fun query(filter: (RealmQuery<T>) -> Unit): List<T> {
        val realmQuery = realm.where(realmClass)
        filter(realmQuery)
        val results = realmQuery.findAll()
        return realm.copyFromRealm(results)
    }

    // Fetch all items as JSON strings
    fun fetchAllAsJson(): List<String> {
        val results = realm.where(realmClass).findAll()
        return realm.copyFromRealm(results).map { gson.toJson(it) }
    }

    // Fetch a single item as a JSON string
    fun fetchByIdAsJson(id: String): String? {
        val item = realm.where(realmClass).equalTo("id", id).findFirst()?.let {
            realm.copyFromRealm(it)
        }
        return item?.let { gson.toJson(it) }
    }

    // Update a specific field
    fun updateField(id: String, fieldName: String, newValue: Any) {
        realm.executeTransaction { transactionRealm ->
            val item = transactionRealm.where(realmClass).equalTo("id", id).findFirst()
            item?.let {
                val field = it.javaClass.getDeclaredField(fieldName)
                field.isAccessible = true
                field.set(it, newValue)
            }
        }
    }

    // Check if an item exists by ID
    fun exists(id: String): Boolean {
        return realm.where(realmClass).equalTo("id", id).findFirst() != null
    }

    // Get distinct values for a specific field
    fun getDistinctValues(fieldName: String): List<Any?> {
        return realm.where(realmClass)
            .distinct(fieldName)
            .findAll()
            .map { it.javaClass.getDeclaredField(fieldName).apply { isAccessible = true }.get(it) }
    }

    // Perform a paginated query
    fun fetchPaginated(limit: Int, offset: Int): List<T> {
        val results = realm.where(realmClass).findAll()
        return realm.copyFromRealm(results.subList(offset, (offset + limit).coerceAtMost(results.size)))
    }

    fun Any.toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    // Clean up Realm instance
    override fun onCleared() {
        super.onCleared()
        realm.close()
    }
}
