package com.snofed.publicapp.ridelog

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.util.UUID



@Parcelize
@RealmClass
open class NewWorkoutPoint(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var workoutRef: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var speed: Double = 0.0,
    var timestamp: String = ""
) : RealmObject(), Parcelable {

    companion object : Parceler<NewWorkoutPoint> {

        override fun NewWorkoutPoint.write(dest: Parcel, flags: Int): Nothing = TODO()
        override fun create(parcel: Parcel): NewWorkoutPoint {
            return NewWorkoutPoint(parcel.toString())
        }
    }
}

