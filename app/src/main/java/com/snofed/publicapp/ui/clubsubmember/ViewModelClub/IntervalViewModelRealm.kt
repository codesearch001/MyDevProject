package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import com.snofed.publicapp.models.realmModels.Interval

open class IntervalViewModelRealm(private val realmRepository: RealmRepository) {

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
