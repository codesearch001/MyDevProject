package com.snofed.publicapp.ridelog

import android.os.Parcel
import android.os.Parcelable
import com.snofed.publicapp.utils.enums.SyncActionEnum
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
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var speed: Double = 0.0,
    var timestamp: String = "",
    var lengthFromPrevPoint: Double = 0.0,
    var syncAction: Int = SyncActionEnum.NEW.getValue(),
    var workoutRef: String = "",
) : RealmObject(), Parcelable {

    companion object : Parceler<NewWorkoutPoint> {

        override fun NewWorkoutPoint.write(dest: Parcel, flags: Int): Nothing = TODO()
        override fun create(parcel: Parcel): NewWorkoutPoint {
            return NewWorkoutPoint(parcel.toString())
        }
    }
}

