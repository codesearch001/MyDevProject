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
open class NewWorkoutImage(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var syncAction: Int = 0,
    var path: String = "",
    var fileName: String = "",
    var workout_id: String = ""
) : RealmObject(), Parcelable {

    companion object : Parceler<NewWorkoutImage> {

        override fun NewWorkoutImage.write(dest: Parcel, flags: Int): Nothing = TODO()
        override fun create(parcel: Parcel): NewWorkoutImage {
            return NewWorkoutImage(parcel.toString())
        }
    }
}

