import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery


// Generic Realm Repository class
open class RealmRepository {

    // Get a new Realm instance for every operation
    fun getRealmInstance(): Realm {
        return Realm.getDefaultInstance()
    }

    // Insert or update a single RealmObject
    fun <T : RealmObject> insertOrUpdate(model: T) {
        getRealmInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(model)
            }
        }
    }

    // Insert or update a list of RealmObjects
    fun <T : RealmObject> insertOrUpdateAll(models: List<T>) {
        getRealmInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                models.forEach { model ->
                    transactionRealm.insertOrUpdate(model)
                }
            }
        }
    }

    // Retrieve a specific RealmObject by its primary key
    fun <T : RealmObject> getById(clazz: Class<T>, primaryKey: String): T? {
        getRealmInstance().use { realm ->
            return realm.where(clazz).equalTo("id", primaryKey).findFirst()
        }
    }

    // Retrieve all objects of a given model
    fun <T : RealmObject> getAll(clazz: Class<T>): List<T> {
        getRealmInstance().use { realm ->
            return realm.where(clazz).findAll()
        }
    }

    // Delete a specific RealmObject by its primary key
    fun <T : RealmObject> deleteById(clazz: Class<T>, primaryKey: String) {
        getRealmInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                val objectToDelete = transactionRealm.where(clazz).equalTo("id", primaryKey).findFirst()
                objectToDelete?.deleteFromRealm()
            }
        }
    }

    // Delete all objects of a given model
    fun <T : RealmObject> deleteAll(clazz: Class<T>) {
        getRealmInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.where(clazz).findAll().deleteAllFromRealm()
            }
        }
    }

    // Query objects with custom conditions
    fun <T : RealmObject> query(clazz: Class<T>, query: RealmQuery<T>.() -> Unit): List<T> {
        getRealmInstance().use { realm ->
            return realm.where(clazz).apply(query).findAll()
        }
    }

    // Count all objects of a given type
    fun <T : RealmObject> count(clazz: Class<T>): Long {
        getRealmInstance().use { realm ->
            return realm.where(clazz).count()
        }
    }
}

