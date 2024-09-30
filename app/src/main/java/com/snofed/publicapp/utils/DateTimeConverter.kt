package com.snofed.publicapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeConverter {
    // Property to hold the formatted time part
    var dateandtimePart: String = ""
    var outputFormatterOnlyDate: String = ""

    var datePartOnly: String = ""
    var dateOfMonthPartOnly: String = ""
    var dateNewPartOnly: String = ""
    var timePart: String = ""
    var datePart: String = ""
    var dateMonthPartOnly: String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    private val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy ")
    // Formatter for converting to desired format
    @RequiresApi(Build.VERSION_CODES.O)
    private val outputFormatterDateTime: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputFormatterDate: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    @RequiresApi(Build.VERSION_CODES.O)
    private val outputDateFormatter = DateTimeFormatter.ofPattern("dd")
    @RequiresApi(Build.VERSION_CODES.O)
    private val outputDateOfMonthFormatter = DateTimeFormatter.ofPattern("MMM")

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputDateMonthFormatter = DateTimeFormatter.ofPattern("MMMM")

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputDateNewFormatter = DateTimeFormatter.ofPattern("dd MMM")

    @RequiresApi(Build.VERSION_CODES.O)
    private val outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm 'h'")


    //"HH:mm:ss"
    fun formatSecondsToHMS(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    fun calculateSpeedFromPace(pace: Double): Double {
        // Calculate speed as distance per time unit (e.g., miles per hour)
        return 60.0 / pace
    }

    //"dd/MM/yyyy hh:mm a"
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTime(inputDateTimeString: String): String {

        // Parse input string into LocalDateTime
        val dateTime = LocalDateTime.parse(inputDateTimeString, inputFormatter)
        // Format date part
        dateandtimePart = dateTime.format(outputFormatterDateTime)
        outputFormatterOnlyDate = dateTime.format(outputFormatterDate)
        datePart = dateTime.format(outputFormatter)
        // Format date part
        datePartOnly = dateTime.format(outputDateFormatter)
        dateOfMonthPartOnly = dateTime.format(outputDateOfMonthFormatter)
        dateMonthPartOnly = dateTime.format(outputDateMonthFormatter)
        // Format date part
        dateNewPartOnly = dateTime.format(outputDateNewFormatter)
        // Format time part
        timePart = dateTime.format(outputTimeFormatter)
        // Combine formatted date and time parts
        return "$datePart, $timePart"
    }
}
