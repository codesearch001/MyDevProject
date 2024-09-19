/*
package com.snofed.publicapp.maps


import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmObject

import java.io.Serializable

import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.UUID


////////////////////////////////////////

@RealmClass
open class NewRideWorkout(
    @PrimaryKey
    var id: String = "",
    var syncAction: Int = 0,
    var publicUserId: String = "",
    var publisherFullname: String = "",
    var duration: Int = 0,
    var distance: Double = 0.0,
    var isPublic: Boolean = true,
    var isNewlyCreated: Boolean = true,
    var averagePace: Double = 0.0,
    var startTime: String = "",
    var calories: Int = 0,
    var activityId: String = "",
    var description: String = "",
    var workoutPoints: RealmList<NewWorkoutPoint> = RealmList(),
    var leaderboardTime: Int = 0,
    var workoutImages: RealmList<NewWorkoutImage> = RealmList()
) : RealmObject()


@RealmClass
open class NewWorkoutPoint(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var workoutRef: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var speed: Double = 0.0,
    var timestamp: String = ""
) : RealmObject()


@RealmClass
open class NewWorkoutImage(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var syncAction: Int = 0,
    var path: String = "",
    var fileName: String = "",
    var workoutId: String = ""
) : RealmObject()

@RealmClass
open class NewActivity(
    @PrimaryKey
    var id: String = "",
    var syncAction: Int = 0,
    var name: String = "",
    var description: String = "",
    var coverPath: String = "",
    var iconPath: String = "",
    var available: Boolean = true,
    var intervalType: Int = 0
) : RealmModel
///////////////////////////////////////////

//@RealmClass
//open class NewRideWorkout(
//    @PrimaryKey
//    var id: String = UUID.randomUUID().toString(),
//    var syncAction: Int = 0,
//    var publicUserId: String = "",
//    var publisherFullname: String = "",
//    var duration: Int = 0,
//    var distance: Double = 0.0,
//    var isPublic: Boolean = true,
//    var isNewlyCreated: Boolean = true,
//    var averagePace: Double = 0.0,
//    var startTime: String = "",
//    var calories: Int = 0,
//    var activityId: String = "",
//    var description: String = "",
//    var workoutPoints: RealmList<NewWorkoutPoint> = RealmList(),
//    var leaderboardTime: Int = 0,
//    var workoutImages: RealmList<NewWorkoutImage> = RealmList()
//) : RealmObject(), Serializable
//
//@RealmClass
//open class NewWorkoutPoint(
//    @PrimaryKey
//    var id: String = UUID.randomUUID().toString(),
//    var workoutRef: String = "",
//    var longitude: Double = 0.0,
//    var latitude: Double = 0.0,
//    var speed: Double = 0.0,
//    var timestamp: String = ""
//) : RealmObject(), Serializable
//
//@RealmClass
//open class NewWorkoutImage(
//    @PrimaryKey
//    var id: String = UUID.randomUUID().toString(),
//    var syncAction: Int = 0,
//    var path: String = "",
//    var fileName: String = "",
//    var workoutId: String = ""
//) : RealmObject(), Serializable



























/////////////////////////
*/
/*@Parcelize
open class Workout(
    @PrimaryKey var id: String? = null,
    var duration: Int = 0,
    var distance: Double = 0.0,
    var isPublic: Boolean = false,
    var averagePace: Double = 0.0,
    var startTime: String? = null,
    var calories: Double = 0.0,
    var description: String? = null,
    var publisherFullname: String? = null,
    var isNewlyCreated: Boolean = false,
    var syncAction: Int = 0,
    var publicUserId: String? = null,
    var activityId: String? = null,
    var workoutPoints: RealmList<WorkoutPoint> = RealmList(),
    var workoutImages: RealmList<WorkoutImage> = RealmList()
) : RealmObject(), Parcelable {
    companion object CREATOR : Parcelable.Creator<Workout> {
        override fun createFromParcel(parcel: Parcel): Workout {
            return Workout(
                id = parcel.readString(),
                duration = parcel.readInt(),
                distance = parcel.readDouble(),
                isPublic = parcel.readByte() != 0.toByte(),
                averagePace = parcel.readDouble(),
                startTime = parcel.readString(),
                calories = parcel.readDouble(),
                description = parcel.readString(),
                isNewlyCreated = parcel.readByte() != 0.toByte(),
                publisherFullname = parcel.readString(),
                syncAction = parcel.readInt(),
                publicUserId = parcel.readString(),
                activityId = parcel.readString(),
                workoutPoints = RealmList<WorkoutPoint>().apply {
                    parcel.readTypedList(this, WorkoutPoint.CREATOR)
                },
                workoutImages = RealmList<WorkoutImage>().apply {
                    parcel.readTypedList(this, WorkoutImage.CREATOR)
                }
            )
        }

        override fun newArray(size: Int): Array<Workout?> {
            return arrayOfNulls(size)
        }
    }
}*//*



*/
