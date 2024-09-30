package com.snofed.publicapp.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreference {

    private var sharedPreferences: SharedPreferences.Editor? = null
    private const val PREFERENCE_NAME = "app-preference"

    fun savePreference(context: Context?, key: String?, value: String?) {
        if (context != null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            sharedPreferences?.putString(key, value)
            sharedPreferences?.apply()

        }

    }

    fun getPreference(context: Context?, key: String?): String? {
        return if (context != null) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            sharedPreferences.getString(key, "")
        } else {
            ""
        }
    }


    fun clearPreference(context: Context) {
        val userToken = getPreference(context, SharedPreferenceKeys.USER_TOKEN)
        sharedPreferences?.clear()
        sharedPreferences?.apply()
        savePreference(context,SharedPreferenceKeys.USER_TOKEN,userToken)
       /* val selectedLang = getPreference(context, SharedPreferenceKeys.SELECTED_LANGUAGE)
        val cb = getPreference(context, SharedPreferenceKeys.IS_CREDENTIAL_REMEMBER_CB)
        val email = getPreference(context, SharedPreferenceKeys.USER_EMAIL_ID)
        val password = getPreference(context, SharedPreferenceKeys.USER_PASSWORD)

        savePreference(context,SharedPreferenceKeys.SELECTED_LANGUAGE,selectedLang)
        savePreference(context,SharedPreferenceKeys.IS_CREDENTIAL_REMEMBER_CB,cb)
        savePreference(context,SharedPreferenceKeys.USER_EMAIL_ID,email)
        savePreference(context,SharedPreferenceKeys.USER_PASSWORD,password)*/
    }
}