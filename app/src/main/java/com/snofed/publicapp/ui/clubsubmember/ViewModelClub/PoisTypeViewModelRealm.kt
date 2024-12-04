package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import androidx.lifecycle.ViewModel
import com.snofed.publicapp.models.realmModels.PoiType
import io.realm.Realm

open class PoisTypeViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }
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

    fun getIconPathByPoiTypeId(poiTypeId: String): String? {
        val poiType = getPoiTypeById(poiTypeId)
        return poiType?.iconPath
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
