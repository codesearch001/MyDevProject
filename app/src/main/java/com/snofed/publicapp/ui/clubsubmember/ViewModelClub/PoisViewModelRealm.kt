package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import androidx.lifecycle.ViewModel
import com.snofed.publicapp.models.realmModels.Poi
import io.realm.Realm

open class PoisViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }
    fun getPoiById(poiId: String): Poi? {
        return realmRepository.getById(Poi::class.java, poiId)
    }

    fun getPoisByClientId(clientId: String): List<Poi> {
        return realmRepository.query(Poi::class.java) {
            equalTo("clientId", clientId)
        }
    }
    fun getDistinctPioTypesByClientId(clientId: String): List<String> {
        return getPoisByClientId(clientId).map { it.poiTypeId!! }.distinct()
    }

    fun addOrUpdatePoi(poi: Poi) {
        realmRepository.insertOrUpdate(poi)
    }

    fun addOrUpdatePoisTypes(pois: List<Poi>) {
        realmRepository.insertOrUpdateAll(pois)
    }

    fun getAllPois(): List<Poi> {
        return realmRepository.getAll(Poi::class.java)
    }

    fun deletePoiById(poiId: String) {
        realmRepository.deleteById(Poi::class.java, poiId)
    }

    fun deleteAllPois() {
        realmRepository.deleteAll(Poi::class.java)
    }

    fun countAllPois(): Long {
        return realmRepository.count(Poi::class.java)
    }

    fun getPoisByField(fieldName: String, value: String): List<Poi> {
        return realmRepository.query(Poi::class.java) {
            equalTo(fieldName, value)
        }
    }
}