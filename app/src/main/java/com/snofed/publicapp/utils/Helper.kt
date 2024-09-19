package com.snofed.publicapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import com.snofed.publicapp.utils.Constants.MAX_HEIGHT_WIDTH
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToLong

class Helper {


    object PrefsManager {
        private const val PREFS_NAME = "app_prefs"
        private const val KEY_FIRST_TIME = "is_first_time"

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

        fun isFirstTime(context: Context): Boolean {
            val prefs = getSharedPreferences(context)
            return prefs.getBoolean(KEY_FIRST_TIME, true)
        }

        fun setFirstTime(context: Context, isFirstTime: Boolean) {
            val prefs = getSharedPreferences(context)
            prefs.edit().putBoolean(KEY_FIRST_TIME, isFirstTime).apply()
        }
    }


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
        fun getDateNow(format: String): String {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(Date())
        }


        fun m2Km(m: Double?): Double {
            if (m != null) {
                return m / 1000
            }
            return 0.0
        }


        fun convertTimeToSeconds(timeString: String): Int {
            val timeParts = timeString.split("")
            return (timeParts[0].toInt() * 3600) + (timeParts[1].toInt() * 60) + timeParts[2].toInt()
        }


        fun round(value: Double, places: Int): Double {
            require(places >= 0) { "Decimal places cannot be negative" }
            val factor = 10.0.pow(places)
            var scaledValue = value * factor
            scaledValue = scaledValue.roundToLong().toDouble()
            return scaledValue / factor
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


    fun resizeImage(context: Context, fileName: String): Bitmap? {
        var resizedImage: Bitmap? = null
        var out: FileOutputStream? = null

        try {
            val file = File(context.filesDir.absoluteFile.toString() + File.separator + "workout_images" + File.separator + fileName)
            if (file.isFile) {
                Log.d("IMAGE FILE", "is present")
            } else {
                Log.d("IMAGE FILE", "not present")
            }

            val options = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.RGB_565
            }
            val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)

            var scale = 1
            val width = bitmap.width
            val height = bitmap.height

            if (width > height) {
                while (width / scale / 2 >= MAX_HEIGHT_WIDTH) {
                    scale *= 2
                }
            } else {
                while (height / scale / 2 >= MAX_HEIGHT_WIDTH) {
                    scale *= 2
                }
            }

            val options2 = BitmapFactory.Options().apply {
                inSampleSize = scale
                inPreferredConfig = Bitmap.Config.RGB_565
            }
            resizedImage = BitmapFactory.decodeStream(FileInputStream(file), null, options2)

            // Rotating image
            val exif = ExifInterface(file.absolutePath)
            val orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
            val orientation = orientString?.toInt() ?: ExifInterface.ORIENTATION_NORMAL
            var rotationAngle = 0
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotationAngle = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> rotationAngle = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> rotationAngle = 270
            }

            val matrix = Matrix().apply {
                setRotate(rotationAngle.toFloat(), (resizedImage!!.width / 2).toFloat(), (resizedImage.height / 2).toFloat())
            }
            val rotatedBitmap = resizedImage?.let { Bitmap.createBitmap(resizedImage, 0, 0, it.width, resizedImage.height, matrix, true) }

            out = FileOutputStream(context.filesDir.absoluteFile.toString() + File.separator + "workout_images" + File.separator + fileName)
            rotatedBitmap?.compress(Bitmap.CompressFormat.PNG, 90, out)

            return rotatedBitmap
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

}