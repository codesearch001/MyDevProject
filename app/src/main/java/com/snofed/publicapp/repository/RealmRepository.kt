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
}

