package com.snofed.publicapp.utils

import android.Manifest
import android.app.AlarmManager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.ResultReceiver
import android.os.SystemClock
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient

import com.snofed.publicapp.R

/*
class LocationTrackingService: LifecycleService() {

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val NOTIFICATION_CHANNEL_ID = "location_tracking_channel"
        private const val NOTIFICATION_ID = 1
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Handle location updates here
                    // For example, update MapboxMap or store data
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_START -> startTracking()
            ACTION_STOP -> stopTracking()
        }
        return START_STICKY
    }

    private fun startTracking() {
        val notification = createNotification("Tracking location...")
        startForeground(NOTIFICATION_ID, notification)

        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    private fun createNotification(contentText: String): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Location Tracking",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Location Tracking")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.map_location_icon) // Replace with your notification icon
            .setOngoing(true)
            .build()
    }}*/
class RecordWorkoutService : LifecycleService() {

    companion object {
        private const val TAG = "RecordWorkoutService"
        private const val UPDATE_INTERVAL_IN_MILLISECONDS = 4000L
        private const val DISTANCE_IN_METERS = 2
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
        var isPaused = false
        private var showAutoPausedNotification = true
    }

    private var WORKOUT_MESSAGE: String? = null
    private var WORKOUT_PAUSED: String? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation:  android.location.Location? = null
    private var mRequestingLocationUpdates: Boolean? = null
    private var bundle: Bundle? = null
    private var receiver: ResultReceiver? = null
    private var isRemoved = false
    private var isAutoPaused = false

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate")
        // Initialize lifecycle observer or other components if needed
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.e(TAG, "onStartCommand")

        if (intent != null && intent.action != null) {
            when (intent.action) {
                SnofedConstants.ACTION_RECORDING -> {
                    Log.e(TAG, "onStartCommand: SERVICE RECORDING")
                    isAutoPaused = intent.getBooleanExtra(SnofedConstants.WORKOUT_AUTO_PAUSE, false)
                    WORKOUT_MESSAGE = intent.getStringExtra(SnofedConstants.WORKOUT_RECORDING)
                    startTrailRecording(intent)
                }
                SnofedConstants.ACTION_RECORDING_PAUSED -> {
                    Log.e(TAG, "onStartCommand: SERVICE PAUSED")
                    isPaused = true
                    WORKOUT_PAUSED = intent.getStringExtra(SnofedConstants.WORKOUT_PAUSED)
                    //startForegroundNotification()
                }
                SnofedConstants.ACTION_TRIGGER_AUTO_PAUSED -> {
                    Log.e(TAG, "onStartCommand: SERVICE AUTO PAUSED TURNED ON")
                    showAutoPausedNotification = true
                    sendAutoPaused()
                    //startForegroundNotification()
                }
                else -> throw IllegalStateException("Unexpected value: ${intent.action}")
            }
        } else if (isPaused && isRemoved) {
            Log.e(TAG, "onStartCommand: SERVICE REMOVED IN PAUSE MODE")
            isPaused = true
           // startForegroundNotification()
        } else {
            Log.e(TAG, "onStartCommand: SERVICE REMOVED IN RECORDING MODE")
            if (intent != null) {
                startTrailRecording(intent)
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy: ")
        stopLocationUpdates()
        super.onDestroy()
    }

    private fun startTrailRecording(intent: Intent) {
        isPaused = false
        mRequestingLocationUpdates = false

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)

        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()

        //startForegroundNotification()

        bundle = Bundle()
        receiver = intent.getParcelableExtra(SnofedConstants.RECEIVER)

        startLocationUpdates()
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = DISTANCE_IN_METERS.toFloat()
        }
    }

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation

                if (isAutoPaused) {
                    val speedKmh = mCurrentLocation?.speed?.times(3.6) ?: 0.0
                    if (speedKmh > PreferencesHelper.getCurrentAutoPauseSpeed(this@RecordWorkoutService)) {
                        if (showAutoPausedNotification) {
                            showAutoPausedNotification = false
                            //startForegroundNotification()
                        }
                    } else {
                        if (!showAutoPausedNotification) {
                            showAutoPausedNotification = true
                            //startForegroundNotification()
                        }
                    }
                } else {
                    showAutoPausedNotification = false
                }
                sendCoordinates(showAutoPausedNotification)
                Log.e(TAG, "onLocationResult: lat: ${mCurrentLocation?.latitude}, long: ${mCurrentLocation?.longitude}, speed: ${mCurrentLocation?.speed?.times(3.6)}, date: ${SnofedUtils.getDateNow(SnofedConstants.DATETIME_FORMAT)}")
            }
        }
    }

    private fun startLocationUpdates() {
        mSettingsClient?.checkLocationSettings(mLocationSettingsRequest!!)
            ?.addOnSuccessListener {
                Log.i(TAG, "All location settings are satisfied.")

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return@addOnSuccessListener
                }
                mFusedLocationClient?.requestLocationUpdates(mLocationRequest!!, mLocationCallback!!, Looper.myLooper())
            }
    }

    private fun stopLocationUpdates() {
        Log.e(TAG, "stopLocationUpdates: stopping")
        if (!isPaused) {
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback!!)
                ?.addOnCompleteListener {
                    mRequestingLocationUpdates = false
                }
            receiver?.send(SnofedConstants.STATUS_FINISHED, null)
        }
    }

    private fun sendAutoPaused() {
        receiver?.send(SnofedConstants.STATUS_AUTO_PAUSED, bundle)
    }

    private fun sendCoordinates(isAutoPausedON: Boolean) {
        bundle?.apply {
            putDouble(SnofedConstants.WORKOUT_LATITUDE, mCurrentLocation?.latitude ?: 0.0)
            putDouble(SnofedConstants.WORKOUT_LONGITUDE, mCurrentLocation?.longitude ?: 0.0)
            putDouble(SnofedConstants.WORKOUT_ALTITUDE, mCurrentLocation?.altitude ?: 0.0)
           // putFloat(SnofedConstants.WORKOUT_SPEED, mCurrentLocation?.speed?: 0)
            putString(SnofedConstants.WORKOUT_DATE_TIME, SnofedUtils.getDateNow(SnofedConstants.DATETIME_FORMAT))

            receiver?.send(if (!isAutoPausedON) SnofedConstants.STATUS_PROCESSING else SnofedConstants.STATUS_PAUSED, this)
        }
    }

/*    private fun startForegroundNotification() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(SnofedConstants.NOTIF_CHANNEL_ID, SnofedConstants.NOTIF_CHANNEL_ID, importance)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            val notificationIntent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val contextText = when {
            isAutoPaused && showAutoPausedNotification -> getString(R.string.recording_workout_auto_pause)
            isPaused -> WORKOUT_PAUSED
            else -> WORKOUT_MESSAGE
        }

        Log.e(TAG, "startForegroundNotification: $contextText")
        val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_notificaton_skotersverige)

        startForeground(SnofedConstants.NOTIF_ID, NotificationCompat.Builder(this, SnofedConstants.NOTIF_CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_notificaton_skotersverige)
            .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
            .setContentTitle(getString(R.string.app_name))
            .setContentText(contextText)
            .setContentIntent(pendingIntent)
            .build())
    }*/

    override fun onTaskRemoved(rootIntent: Intent) {
        Log.e(TAG, "onTaskRemoved: ")

        if (!isPaused) {
            stopLocationUpdates()
        } else {
            isRemoved = true
        }

        val restartServiceIntent = Intent(applicationContext, this::class.java).apply {
            setPackage(packageName)
        }
        val restartServicePendingIntent = PendingIntent.getService(applicationContext, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val alarmService = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), restartServicePendingIntent)

        super.onTaskRemoved(rootIntent)
    }
}
