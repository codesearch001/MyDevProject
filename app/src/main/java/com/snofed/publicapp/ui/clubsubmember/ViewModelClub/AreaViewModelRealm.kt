package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import androidx.lifecycle.ViewModel
import com.snofed.publicapp.models.realmModels.Area
import com.snofed.publicapp.repository.RealmRepository
import io.realm.Case
import io.realm.Realm

open class AreaViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }

    fun getAreaById(areaId: String): Area? {
        return realmRepository.getById(Area::class.java, areaId)
    }

    fun getAreasByClientId(clientId: String): List<Area> {
        return realmRepository.query(Area::class.java) {
            equalTo("clientId", clientId, Case.INSENSITIVE)
        }
    }

    fun addOrUpdateArea(area: Area) {
        realmRepository.insertOrUpdate(area)
    }

    fun addOrUpdateArea(area: List<Area>) {
        realmRepository.insertOrUpdateAll(area)
    }

    fun getAllArea(): List<Area> {
        return realmRepository.getAll(Area::class.java)
    }

    fun deleteAreaById(areaId: String) {
        realmRepository.deleteById(Area::class.java, areaId)
    }

    fun deleteAllArea() {
        realmRepository.deleteAll(Area::class.java)
    }

    fun countAllArea(): Long {
        return realmRepository.count(Area::class.java)
    }

    fun getAreaByField(fieldName: String, value: String): List<Area> {
        return realmRepository.query(Area::class.java) {
            equalTo(fieldName, value)
        }
    }
}