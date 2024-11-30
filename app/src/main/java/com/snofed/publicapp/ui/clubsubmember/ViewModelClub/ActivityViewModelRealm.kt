package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import com.snofed.publicapp.models.realmModels.Activities

open class ActivityViewModelRealm(private val realmRepository: RealmRepository) {

    fun getActivityById(activityId: String): Activities? {
        return realmRepository.getById(Activities::class.java, activityId)
    }

    fun addOrUpdateActivity(activity: Activities) {
        realmRepository.insertOrUpdate(activity)
    }

    fun addOrUpdateActivities(activities: List<Activities>) {
        realmRepository.insertOrUpdateAll(activities)
    }

    fun getAllActivities(): List<Activities> {
        return realmRepository.getAll(Activities::class.java)
    }

    fun deleteActivityById(activityId: String) {
        realmRepository.deleteById(Activities::class.java, activityId)
    }

    fun deleteAllActivities() {
        realmRepository.deleteAll(Activities::class.java)
    }

    fun countAllActivities(): Long {
        return realmRepository.count(Activities::class.java)
    }

    fun getActivitiesByField(fieldName: String, value: String): List<Activities> {
        return realmRepository.query(Activities::class.java) {
            equalTo(fieldName, value)
        }
    }
}
