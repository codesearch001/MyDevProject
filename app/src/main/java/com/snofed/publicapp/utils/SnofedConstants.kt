package com.snofed.publicapp.utils

class SnofedConstants {

    // SERVICE ACTIONS
    companion object {

        const val FIRST_TIME_APP_SYNC = "app_sync_for_a_first_time"

        const val SNOFED_PREFS = "snofed_prefs"
         const val PREFS_NAME = "app_preferences"
         const val FIRST_TIME_APP_USE = "first_time_app_use"

        const val ACTION_RECORDING: String = "action_recording"

        const val ACTION_RECORDING_PAUSED: String = "action_recording_paused"

        const val ACTION_TRIGGER_AUTO_PAUSED: String = "action_auto_paused_on"

        const val WORKOUT_RECORDING: String = "workout_recording"

        const val WORKOUT_PAUSED: String = "workout_paused"

        const val WORKOUT_AUTO_PAUSE: String = "workout_auto_pause"

        const val WORKOUT_AUTO_PAUSE_SPEED: String = "workout_auto_pause_speed"

        const val WORKOUT_DATE_TIME: String = "dateTime"

        const val WORKOUT_SPEED: String = "workout_speed"

        const val WORKOUT_LATITUDE: String = "latitude"

        const val WORKOUT_LONGITUDE: String = "longitude"

        const val WORKOUT_ALTITUDE: String = "altitude"

        const val IS_AUTO_PAUSED_ON: String = "is_auto_paused_on"

        const val SNOFED_WORKOUT_IMG_PATH: String = "Android/data/com.itengine.snofed/WorkoutImages"

        const val RECEIVER: String = "receiver"


        // SERVICE LOCATION

        const val STATUS_PROCESSING: Int = 90

        const val STATUS_PAUSED: Int = 91

        const val STATUS_FINISHED: Int = 92

        const val STATUS_AUTO_PAUSED: Int = 93

        // LATITUDE AND LONGITUDE
        const val CENTER_LAT = 62.387500
        
        const val CENTER_LONG = 16.325556
        // DATETIME FORMAT

        const val DATETIME_FORMAT: String = "yyyy-MM-dd'T'HH:mm:ss"

        const val DATETIME_FORMAT_RETROFIT: String = "yyyy-MM-dd HH:mm:ss.SSS"

        const val DATETIME_SERVER_FORMAT: String = "yyyy-MM-dd'T'HH:mm:ss.SSS"


    }

}