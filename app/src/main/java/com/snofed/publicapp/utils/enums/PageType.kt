package com.snofed.publicapp.utils.enums

import android.os.Parcel
import android.os.Parcelable


enum class PageType : Parcelable {
    DETAIL,
    MAP;

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<PageType> {
            override fun createFromParcel(parcel: Parcel): PageType {
                return values()[parcel.readInt()]
            }

            override fun newArray(size: Int): Array<PageType?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }
}