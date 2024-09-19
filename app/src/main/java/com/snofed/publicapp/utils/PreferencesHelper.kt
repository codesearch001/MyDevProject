package com.snofed.publicapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.snofed.publicapp.R
import kotlin.math.roundToInt

class PreferencesHelper {

    companion object {
        private const val TAG = "PreferencesHelper"

        fun getCurrentThemeString(context: Context): String {
            val themeItems = context.resources.getStringArray(R.array.pref_theme_items)
            val currentValue = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_THEME, "0")?.toInt() ?: 0
            return themeItems[currentValue]
        }

        fun getCurrentAutoPauseSpeedString(context: Context): String {
            val autoPauseSpeedItems = context.resources.getStringArray(R.array.pref_autopause_speed_items)
            val currentValue = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_AUTOPAUSE_SPEED, "0")?.toInt() ?: 0
            return try {
                autoPauseSpeedItems[currentValue]
            } catch (e: IndexOutOfBoundsException) {
                Log.e(TAG, e.message ?: "Index out of bounds")
                autoPauseSpeedItems[0]
            }
        }

        fun getCurrentAutoPauseSpeed(context: Context): Int {
            val value = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_AUTOPAUSE_SPEED, "0")?.toInt() ?: 0
            return if (value == 0) 2 else 4
        }

        fun getCurrentUnitsString(context: Context): String {
            val unitsItems = context.resources.getStringArray(R.array.pref_units_items)
            val currentValue = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_UNITS, "0")?.toInt() ?: 0
            return unitsItems[currentValue]
        }

        fun getCurrentUserAgeString(context: Context): String {
            val currentValue = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PreferencesNames.PREF_USER_AGE, 0)
            return if (currentValue <= 2014) {
                currentValue.toString()
            } else {
                context.getString(R.string.not_available)
            }
        }

        fun getCurrentUserGenderString(context: Context): String {
            val genderItems = context.resources.getStringArray(R.array.pref_gender_items)
            val currentValue = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_USER_GENDER, "0")?.toInt() ?: 0
            return genderItems[currentValue]
        }

        fun getCurrentUserWeightString(context: Context): String {
            var currentValue = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PreferencesNames.PREF_USER_WEIGHT, 0)
            val unitsValue = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_UNITS, "0")?.toInt() ?: 0

            if (currentValue < 1) {
                return context.getString(R.string.not_available)
            }

            if (unitsValue != 0) {
                currentValue = kilogramToPound(currentValue)
            }
            val unitString = if (unitsValue == 0) {
                context.getString(R.string.pref_user_kg_units)
            } else {
                context.getString(R.string.pref_user_lbs_units)
            }
            return "$currentValue $unitString"
        }

        fun getCurrentUserWeightTitle(context: Context): String {
            val baseTitle = context.getString(R.string.pref_user_weight_title)
            val unitsValue = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_UNITS, "0")?.toInt() ?: 0
            val unitString = if (unitsValue == 0) {
                context.getString(R.string.pref_user_kg_units)
            } else {
                context.getString(R.string.pref_user_lbs_units)
            }
            return "$baseTitle $unitString"
        }

        fun getSpeedUnit(context: Context): String {
            return if (isImperial(context)) {
                context.getString(R.string.pref_units_speed_imperial)
            } else {
                context.getString(R.string.pref_units_speed_metric)
            }
        }

        fun getDistanceUnit(context: Context): String {
            return if (isImperial(context)) {
                context.getString(R.string.pref_units_dist_imperial)
            } else {
                context.getString(R.string.pref_units_dist_metric)
            }
        }

        fun getPaceUnit(context: Context): String {
            return if (isImperial(context)) {
                context.getString(R.string.pref_units_pace_imperial)
            } else {
                context.getString(R.string.pref_units_pace_metric)
            }
        }

        fun getUserAge(context: Context): Int {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PreferencesNames.PREF_USER_AGE, 0)
        }

        fun getUserWeight(context: Context): Double {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PreferencesNames.PREF_USER_WEIGHT, 0).toDouble()
        }

        fun isImperial(context: Context): Boolean {
            val unitsValue = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_UNITS, "0")?.toInt() ?: 0
            return unitsValue != 0
        }

        fun getUserName(context: Context): String {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_USER_FIRST_NAME, "") ?: ""
        }

        fun getUserLastName(context: Context): String {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PreferencesNames.PREF_USER_LAST_NAME, "") ?: ""
        }

        fun poundToKilogram(pounds: Int): Int {
            return (0.4536 * pounds).roundToInt()
        }

        fun kilogramToPound(kilos: Int): Int {
            return (2.2 * kilos).roundToInt()
        }

        fun kmToMiles(km: Double): Double {
            return km * 0.621371
        }

        fun milesToKm(miles: Double): Double {
            return miles * 1.60934
        }
    }
}
