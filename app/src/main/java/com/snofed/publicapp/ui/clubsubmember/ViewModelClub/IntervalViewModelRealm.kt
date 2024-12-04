package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import androidx.lifecycle.ViewModel
import com.snofed.publicapp.models.realmModels.Interval
import io.realm.Realm

open class IntervalViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }
    fun getIntervalById(intervalId: String): Interval? {
        return realmRepository.getById(Interval::class.java, intervalId)
    }

    fun addOrUpdateInterval(interval: Interval) {
        realmRepository.insertOrUpdate(interval)
    }

    fun addOrUpdateIntervals(intervals: List<Interval>) {
        realmRepository.insertOrUpdateAll(intervals)
    }

    fun getAllIntervals(): List<Interval> {
        return realmRepository.getAll(Interval::class.java)
    }

    fun deleteIntervalById(intervalId: String) {
        realmRepository.deleteById(Interval::class.java, intervalId)
    }

    fun deleteAllIntervals() {
        realmRepository.deleteAll(Interval::class.java)
    }

    fun countAllIntervals(): Long {
        return realmRepository.count(Interval::class.java)
    }

    fun getIntervalsByField(fieldName: String, value: String): List<Interval> {
        return realmRepository.query(Interval::class.java) {
            equalTo(fieldName, value)
        }
    }
}
