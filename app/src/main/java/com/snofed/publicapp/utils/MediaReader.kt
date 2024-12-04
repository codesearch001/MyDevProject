package com.snofed.publicapp.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.BitmapShader
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.snofed.publicapp.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date

/**
 * MediaReader handles picking and capturing images, and processes them for display.
 */
class MediaReader(
    private val fragment: Fragment,
    private val listener: OnImageUriReceivedListener
) {

    interface OnImageUriReceivedListener {
        fun onImageUriReceived(uri: Uri)
    }

    private var imageView: ImageView? = null

    /**
     * Sets the ImageView where the selected or captured image will be displayed.
     * Uses Glide to load the image from a URL.
     */
    fun setImageView(imageView: ImageView, imageUrl: String) {
        this.imageView = imageView

        // Load the image using Glide
        Glide.with(fragment) // `fragment` provides the necessary context here
            .load(imageUrl)
            .placeholder(R.drawable.user_profile) // Optional: placeholder image while loading
            .error(R.drawable.user_profile) // Optional: error image if loading fails
            .transform(CircleCrop()) // Circle crop transformation to make the image circular
            .into(imageView) // Set the ImageView
    }

    // Launcher for picking an image from the gallery
    private val pickImageLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        fragment.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let { handleImage(it) }
        }

    // Launcher for capturing an image using the camera
    private val takePictureLauncher: ActivityResultLauncher<Uri> =
        fragment.registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess: Boolean ->
            if (isSuccess) {
                val uri = currentPhotoUri ?: return@registerForActivityResult
                handleImage(uri)
            }
        }

    private var currentPhotoUri: Uri? = null

    // Check and request camera permissions
    fun checkPermissionsAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        } else {
            dispatchTakePictureIntent()
        }
    }

    // Check and request gallery permissions
    fun checkPermissionsAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Scoped Storage (Android 10+), no need for READ_EXTERNAL_STORAGE permission
            dispatchPickImageIntent()
        } else {
            // For Android versions below Android 10 (API level 29), check the READ_EXTERNAL_STORAGE permission
            if (ContextCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                dispatchPickImageIntent()
            }
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // Permission granted, proceed with the desired action
            } else {
                // Handle permission denied
            }
        }

    // Launch camera intent to capture an image
    private fun dispatchTakePictureIntent() {
        val context = fragment.requireContext()
        val photoFile = createImageFile(context)
        currentPhotoUri = photoFile?.let {
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it)
        }
        currentPhotoUri?.let { uri ->
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            takePictureLauncher.launch(uri)
        }
    }

    // Launch gallery picker (for Android 10+)
    private fun dispatchPickImageIntent() {
        pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    // Handle the selected or captured image
    private fun handleImage(uri: Uri) {
        val context = fragment.requireContext()
        val correctedBitmap = getCorrectedBitmap(uri, context)
        val circularBitmap = getCircularBitmap(correctedBitmap)
        imageView?.setImageBitmap(circularBitmap)

        val compressedImage = createAndSaveCompressedImage(context, uri, 1000 * 1024)
        listener.onImageUriReceived(uri)
        Log.e("TAG_ProfileSettingFragment", "onImageUriReceived: File path - ${uri}")
    }

    private fun getCorrectedBitmap(uri: Uri, context: Context): Bitmap {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val exif = ExifInterface(context.contentResolver.openInputStream(uri)!!)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        return rotateBitmap(bitmap, rotationDegrees.toFloat())
    }

    private fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(rotationDegrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val size = Math.min(bitmap.width, bitmap.height)
        val squaredBitmap = Bitmap.createBitmap(bitmap, (bitmap.width - size) / 2, (bitmap.height - size) / 2, size, size)

        val outputBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBitmap)
        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
        return outputBitmap
    }

    private fun createImageFile(context: Context): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun createAndSaveCompressedImage(context: Context, imageUri: Uri, maxFileSize: Int): File? {
        val file = createImageFile(context) ?: return null
        val bitmap = getCorrectedBitmap(imageUri, context)
        saveCompressedBitmap(bitmap, file, maxFileSize / 1024)
        return file
    }

    private fun saveCompressedBitmap(bitmap: Bitmap, file: File, maxFileSize: Int = 1000) {
        var quality = 90
        val outputStream = ByteArrayOutputStream()

        do {
            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            if (outputStream.size() / 1024 <= maxFileSize) break
            quality -= 5
        } while (quality > 0)

        FileOutputStream(file).use {
            it.write(outputStream.toByteArray())
        }
    }
}











