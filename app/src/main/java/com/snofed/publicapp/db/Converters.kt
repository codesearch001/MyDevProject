/*
package com.snofed.publicapp.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.snofed.publicapp.models.browseSubClub.Activity
import com.snofed.publicapp.models.browseSubClub.ClientSetting
import com.snofed.publicapp.models.browseSubClub.PublicData

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromPublicDataList(value: List<PublicData>?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toPublicDataList(value: String?): List<PublicData>? {
        val gson = Gson()
        val listType = object : TypeToken<List<PublicData>>() {}.type
        return gson.fromJson(value, listType)
    }
}
*/
