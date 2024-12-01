import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.RealmResults


// Generic Realm Repository class
open class RealmRepository {

    fun getRealmInstance(): Realm {
        return Realm.getDefaultInstance()
    }

    fun <T : RealmObject> insertOrUpdate(model: T) {
        getRealmInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(model)
            }
        }
    }

    fun <T : RealmObject> insertOrUpdateAll(models: List<T>) {
        getRealmInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(models)
            }
        }
    }

    fun <T : RealmObject> getById(clazz: Class<T>, primaryKey: String): T? {
        return getRealmInstance().use { realm ->
            realm.where(clazz).equalTo("id", primaryKey).findFirst()
        }
    }

    fun <T : RealmObject> getAll(clazz: Class<T>): RealmResults<T> {
        val realm = getRealmInstance() // Leave Realm open for lazy loading
        return realm.where(clazz).findAll()
    }

    fun <T : RealmObject> deleteById(clazz: Class<T>, primaryKey: String) {
        getRealmInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.where(clazz).equalTo("id", primaryKey).findFirst()?.deleteFromRealm()
            }
        }
    }

    fun <T : RealmObject> deleteAll(clazz: Class<T>) {
        getRealmInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.where(clazz).findAll().deleteAllFromRealm()
            }
        }
    }

    fun <T : RealmObject> query(clazz: Class<T>, query: RealmQuery<T>.() -> Unit): RealmResults<T> {
        val realm = getRealmInstance() // Leave Realm open for queries
        return realm.where(clazz).apply(query).findAll()
    }

    fun <T : RealmObject> count(clazz: Class<T>): Long {
        return getRealmInstance().use { realm ->
            realm.where(clazz).count()
        }
    }

    /**
     * Query distinct values of a specific field in the provided class.
     *
     * @param clazz The class to query.
     * @param fieldName The name of the field to get distinct values for.
     * @return A list of distinct values for the specified field.
     */
    fun <T : RealmObject> queryDistinct(clazz: Class<T>, fieldName: String): List<Any?> {
        return getRealmInstance().use { realm ->
            realm.where(clazz)
                .distinct(fieldName) // Query distinct values
                .findAll()
                .map { it -> it.fieldValue(fieldName) }
        }
    }

    /**
     * Helper function to retrieve the field value using reflection.
     *
     * @param fieldName The name of the field to extract the value from.
     */
    private fun <T : RealmObject> T.fieldValue(fieldName: String): Any? {
        return this::class.java.getDeclaredField(fieldName).let { field ->
            field.isAccessible = true
            field.get(this)
        }
    }
}

