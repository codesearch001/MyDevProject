package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import androidx.lifecycle.ViewModel
import com.snofed.publicapp.models.realmModels.Zone
import com.snofed.publicapp.repository.RealmRepository
import io.realm.Case
import io.realm.Realm

open class ZoneViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }
    fun getZoneById(zoneId: String): Zone? {
        return realmRepository.getById(Zone::class.java, zoneId)
    }
    fun getZonesByClientId(clientId: String): List<Zone> {
        return realmRepository.query(Zone::class.java) {
            equalTo("clientId", clientId, Case.INSENSITIVE)
        }
    }
    fun addOrUpdateZone(zone: Zone) {
        realmRepository.insertOrUpdate(zone)
    }

    fun addOrUpdateZones(zones: List<Zone>) {
        realmRepository.insertOrUpdateAll(zones)
    }

    fun getAllZones(): List<Zone> {
        return realmRepository.getAll(Zone::class.java)
    }

    fun deleteZoneById(zoneId: String) {
        realmRepository.deleteById(Zone::class.java, zoneId)
    }

    fun deleteAllZones() {
        realmRepository.deleteAll(Zone::class.java)
    }

    fun countAllZones(): Long {
        return realmRepository.count(Zone::class.java)
    }

    fun getZonesByField(fieldName: String, value: String): List<Zone> {
        return realmRepository.query(Zone::class.java) {
            equalTo(fieldName, value)
        }
    }
}