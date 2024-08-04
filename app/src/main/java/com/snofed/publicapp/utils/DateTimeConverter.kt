package com.snofed.publicapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeConverter {
    // Property to hold the formatted time part
    var datePartOnly: String = ""
    var dateNewPartOnly: String = ""
    var timePart: String = ""
    var datePart: String = ""
    var dateMonthPartOnly: String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    private val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputDateFormatter = DateTimeFormatter.ofPattern("dd MMM")

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputDateMonthFormatter = DateTimeFormatter.ofPattern("MMMM")

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputDateNewFormatter = DateTimeFormatter.ofPattern("dd MMM")

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm 'h'")

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTime(inputDateTimeString: String): String {
        // Parse input string into LocalDateTime
        val dateTime = LocalDateTime.parse(inputDateTimeString, inputFormatter)

        // Format date part
        datePart = dateTime.format(outputFormatter)

        // Format date part
        datePartOnly = dateTime.format(outputDateFormatter)
        dateMonthPartOnly = dateTime.format(outputDateMonthFormatter)
        // Format date part
        dateNewPartOnly = dateTime.format(outputDateNewFormatter)

        // Format time part
        timePart = dateTime.format(outputTimeFormatter)

        // Combine formatted date and time parts
        return "$datePart., $timePart"
    }



}
