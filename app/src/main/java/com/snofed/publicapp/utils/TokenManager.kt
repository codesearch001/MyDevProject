package com.snofed.publicapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.snofed.publicapp.R
import com.snofed.publicapp.utils.Constants.CLUB_CLIENT_ID
import com.snofed.publicapp.utils.Constants.CLUB_DESC
import com.snofed.publicapp.utils.Constants.PREFS_TOKEN_FILE
import com.snofed.publicapp.utils.Constants.USER_FIRST_NAME
import com.snofed.publicapp.utils.Constants.USER_TOKEN
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
        editor.putString(USER_FIRST_NAME, userFirstName)
        editor.apply()
    }

    fun getUser(): String? {
        return prefs.getString(USER_FIRST_NAME, null)
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
}