import io.realm.Realm
import io.realm.RealmObject


// Generic Realm Repository class
class RealmRepository {

    private val realm: Realm = Realm.getDefaultInstance()

    // Insert or update any RealmObject
    fun <T : RealmObject> insertOrUpdate(model: T) {
        realm.executeTransactionAsync { transactionRealm ->
            transactionRealm.insertOrUpdate(model)
        }
    }

    // Retrieve a specific RealmObject by its primary key
    fun <T : RealmObject> getById(clazz: Class<T>, primaryKey: String): T? {
        return realm.where(clazz).equalTo("id", primaryKey).findFirst()
    }

    // Retrieve all objects of a given model
    fun <T : RealmObject> getAll(clazz: Class<T>): List<T> {
        return realm.where(clazz).findAll()
    }

    // Delete a specific RealmObject by its primary key
    fun <T : RealmObject> deleteById(clazz: Class<T>, primaryKey: String) {
        realm.executeTransactionAsync { transactionRealm ->
            val objectToDelete = transactionRealm.where(clazz).equalTo("id", primaryKey).findFirst()
            objectToDelete?.deleteFromRealm()
        }
    }

    // Delete all objects of a given model
    fun <T : RealmObject> deleteAll(clazz: Class<T>) {
        realm.executeTransactionAsync { transactionRealm ->
            transactionRealm.where(clazz).findAll().deleteAllFromRealm()
        }
    }

    // Close the Realm instance when done
    fun close() {
        realm.close()
    }
}
