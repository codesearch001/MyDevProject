package com.snofed.publicapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.snofed.publicapp.R

sealed class ToastType(val message: String) {
    class Success(message: String) : ToastType(message)
    class Error(message: String) : ToastType(message)
    class Info(message: String) : ToastType(message)
}

object ToastUtils {
    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

    @SuppressLint("MissingInflatedId")
    fun showCustomToast(context: Context, toastType: ToastType) {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_toast, null)

        // Set the message text
        val textView = layout.findViewById<TextView>(R.id.toast_text)
        textView.text = toastType.message

        // Customize Toast based on type
        when (toastType) {
            is ToastType.Success -> {
                // Customize for success if needed
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_blue))
            }
            is ToastType.Error -> {
                // Customize for error if needed
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.error_color))
            }
            is ToastType.Info -> {
                // Customize for info if needed
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
        }

        // Create and show the Toast
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}

