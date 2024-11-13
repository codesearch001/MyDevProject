package com.snofed.publicapp.ridelog

import kotlinx.parcelize.Parceler

import android.os.Parcel
import android.os.Parcelable
import com.snofed.publicapp.utils.enums.SyncActionEnum
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.parcelize.Parcelize
import java.util.UUID


@Parcelize
@RealmClass
open class NewRideWorkout(
    @PrimaryKey
    var id: String = "",
    var syncAction: Int = 1,
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
    var workoutImages: RealmList<NewWorkoutImage> = RealmList(),
) : RealmObject(), Parcelable {

    companion object : Parceler<NewRideWorkout> {

        override fun NewRideWorkout.write(dest: Parcel, flags: Int): Nothing = TODO()
        override fun create(parcel: Parcel): NewRideWorkout {
            return NewRideWorkout(parcel.toString())
        }
    }
}


