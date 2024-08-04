package com.snofed.publicapp.utils

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Helper {
    companion object {
        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun hideKeyboard(view: View){
            try {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }catch (e: Exception){

            }
        }


        // Formatter for parsing ISO 8601 date-time string
        @RequiresApi(Build.VERSION_CODES.O)
        private val inputFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        // Formatter for converting to desired format
        @RequiresApi(Build.VERSION_CODES.O)
        private val outputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")

        // Parse the date-time string and format it to the desired pattern
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDateString(dateString: String): String {
            // Parse the input date-time string to LocalDateTime
            val localDateTime = LocalDateTime.parse(dateString, inputFormatter)
            // Format the LocalDateTime to the desired pattern
            return localDateTime.format(outputFormatter)
        }

        // Format and return strings for both start and end dates
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDateRange(startDateString: String, endDateString: String): Pair<String, String> {
            val formattedStartDate = formatDateString(startDateString)
            val formattedEndDate = formatDateString(endDateString)
            return Pair(formattedStartDate, formattedEndDate)
        }
    }

}