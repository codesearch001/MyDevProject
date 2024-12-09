package com.snofed.publicapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.snofed.publicapp.R
import com.snofed.publicapp.utils.Constants.CLUB_CLIENT_ID
import com.snofed.publicapp.utils.Constants.CLUB_DESC
import com.snofed.publicapp.utils.Constants.CLUB_PUBLIC_ID
import com.snofed.publicapp.utils.Constants.CLUB_TRAILS_ID
import com.snofed.publicapp.utils.Constants.PREFS_TOKEN_FILE
import com.snofed.publicapp.utils.Constants.RECORDING_WORKOUT_ID
import com.snofed.publicapp.utils.Constants.USER_FIRST_NAME
import com.snofed.publicapp.utils.Constants.USER_FULL_NAME
import com.snofed.publicapp.utils.Constants.USER_LAST_NAME
import com.snofed.publicapp.utils.Constants.USER_TOKEN
import com.snofed.publicapp.utils.Constants.USER_USER_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUser(userFirstName: String) {
        val editor = prefs.edit()
        editor.putString(USER_FULL_NAME, userFirstName)
        editor.apply()
    }

    fun getFirstName(): String? {
        return prefs.getString(USER_FIRST_NAME, null)
    }

    fun getLastName(): String? {
        return prefs.getString(USER_LAST_NAME, null)
    }

    fun getFullName(): String? {
        return prefs.getString(USER_FULL_NAME, null)
    }

    fun saveUserId(saveUserId: String) {
        val editor = prefs.edit()
        editor.putString(USER_USER_ID, saveUserId)
        editor.apply()
    }

    fun getUserId(): String? {
        return prefs.getString(USER_USER_ID, null)
    }

    fun saveDesc(userDesc: String) {
        val editor = prefs.edit()
        editor.putString(CLUB_DESC, userDesc)
        editor.apply()
    }

    fun getDesc(): String? {
        return prefs.getString(CLUB_DESC, null)
    }

    fun saveClientId(userClient: String) {
        val editor = prefs.edit()
        editor.putString(CLUB_CLIENT_ID, userClient)
        editor.apply()
    }

    fun getClientId(): String? {
        return prefs.getString(CLUB_CLIENT_ID, null)
    }

    fun saveTrailsId(userTrails: String) {
        val editor = prefs.edit()
        editor.putString(CLUB_TRAILS_ID, userTrails)
        editor.apply()
    }

    fun getTrailsId(): String? {
        return prefs.getString(CLUB_TRAILS_ID, null)
    }

    fun savePublicUserId(userPublicID: String) {
        val editor = prefs.edit()
        editor.putString(CLUB_PUBLIC_ID, userPublicID)
        editor.apply()
    }

    fun getPublicUserId(): String? {
        return prefs.getString(CLUB_PUBLIC_ID, null)
    }

////save workout UDID
    fun saveWorkoutUdId(uDID: String) {
        val editor = prefs.edit()
        editor.putString(RECORDING_WORKOUT_ID, uDID)
        editor.apply()
    }

    fun getWorkoutUdId(): String? {
        return prefs.getString(RECORDING_WORKOUT_ID, null)
    }

    // Method to clear session data
    fun clearSession() {
        val editor = prefs.edit()
        editor.remove("token") // Remove the client ID
        editor.remove("clientId") // Remove the user ID
        editor.remove("id") // Remove the user ID
        // Remove any other tokens or data you store
        editor.apply() // Save the changes
    }
}