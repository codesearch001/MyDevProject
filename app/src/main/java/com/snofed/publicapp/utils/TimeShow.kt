package com.snofed.publicapp.utils


import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun String.getTimeAgo(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = try {
        format.parse(this)
    } catch (e: Exception) {
        null
    }

    return if (date != null) {
        val now = Date()
        val diffInMillis = now.time - date.time

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

        when {
            seconds < 60 -> "$seconds seconds ago"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            days == 1L -> "1 day ago"
            days <= 7 -> "$days days ago"
            else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
        }
    } else {
        "Unknown time"
    }
}
