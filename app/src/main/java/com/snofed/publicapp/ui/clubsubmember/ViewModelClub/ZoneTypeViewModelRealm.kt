package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import com.snofed.publicapp.models.realmModels.ZoneType

open class ZoneTypeViewModelRealm(private val realmRepository: RealmRepository) {

    fun getZoneTypeById(zoneTypeId: String): ZoneType? {
        return realmRepository.getById(ZoneType::class.java, zoneTypeId)
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
