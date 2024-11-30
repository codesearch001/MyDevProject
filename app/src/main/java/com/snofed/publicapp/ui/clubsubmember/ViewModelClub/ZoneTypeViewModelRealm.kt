package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import androidx.lifecycle.ViewModel
import com.snofed.publicapp.models.realmModels.Area
import com.snofed.publicapp.models.realmModels.ZoneType
import io.realm.Case
import io.realm.Realm

open class ZoneTypeViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }
    fun getZoneTypeById(zoneTypeId: String): ZoneType? {
        return realmRepository.getById(ZoneType::class.java, zoneTypeId)
    }
    fun getZonesByClientId(clientId: String): List<ZoneType> {
        return realmRepository.query(ZoneType::class.java) {
            equalTo("clientId", clientId, Case.INSENSITIVE)
        }
    }
    fun addOrUpdateZoneType(zoneType: ZoneType) {
        realmRepository.insertOrUpdate(zoneType)
    }

    fun addOrUpdateZoneTypes(zoneTypes: List<ZoneType>) {
        realmRepository.insertOrUpdateAll(zoneTypes)
    }

    fun getAllZoneTypes(): List<ZoneType> {
        return realmRepository.getAll(ZoneType::class.java)
    }

    fun deleteZoneTypeById(zoneTypeId: String) {
        realmRepository.deleteById(ZoneType::class.java, zoneTypeId)
    }

    fun deleteAllZoneTypes() {
        realmRepository.deleteAll(ZoneType::class.java)
    }

    fun countAllZoneTypes(): Long {
        return realmRepository.count(ZoneType::class.java)
    }

    fun getZoneTypesByField(fieldName: String, value: String): List<ZoneType> {
        return realmRepository.query(ZoneType::class.java) {
            equalTo(fieldName, value)
        }
    }
}
