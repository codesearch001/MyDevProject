package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import com.snofed.publicapp.models.realmModels.PoiType

open class PoisTypeViewModelRealm(private val realmRepository: RealmRepository) {

    fun getPoiTypeById(poiTypeId: String): PoiType? {
        return realmRepository.getById(PoiType::class.java, poiTypeId)
    }

    fun addOrUpdatePoiType(poiType: PoiType) {
        realmRepository.insertOrUpdate(poiType)
    }

    fun addOrUpdatePoisTypes(poiTypes: List<PoiType>) {
        realmRepository.insertOrUpdateAll(poiTypes)
    }

    fun getAllPoiTypes(): List<PoiType> {
        return realmRepository.getAll(PoiType::class.java)
    }

    fun deletePoiTypeById(poiTypeId: String) {
        realmRepository.deleteById(PoiType::class.java, poiTypeId)
    }

    fun deleteAllPoiTypes() {
        realmRepository.deleteAll(PoiType::class.java)
    }

    fun countAllPoiTypes(): Long {
        return realmRepository.count(PoiType::class.java)
    }

    fun getPoiTypesByField(fieldName: String, value: String): List<PoiType> {
        return realmRepository.query(PoiType::class.java) {
            equalTo(fieldName, value)
        }
    }
}
