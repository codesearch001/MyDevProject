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
class MediaReader(private val fragment: Fragment, private val imageUriCallback: ImageUriCallback) {



    private var imageView: ImageView? = null
    private var imageUri: Uri? = null // Store the current URI
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
                Log.d("imageView","imageView "+uri)
                handleImage(uri)
            }
        }

    // Current photo URI
    private var currentPhotoUri: Uri? = null

    // Launcher for requesting permissions
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // Handle permission granted
            } else {
                // Handle permission denied
            }
        }

    /**
     * Sets the ImageView where the selected or captured image will be displayed.
     */
    fun setImageView(imageView: ImageView) {

        this.imageView = imageView
    }

    /**
     * Checks if camera permission is granted; if not, requests it.
     * If permission is granted, dispatches the intent to capture a photo.
     */
    fun checkPermissionsAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        } else {
            dispatchTakePictureIntent()
        }
    }

    /**
     * Checks if gallery permission is granted; if not, requests it.
     * If permission is granted, dispatches the intent to pick an image from the gallery.
     */
    fun checkPermissionsAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            dispatchPickImageIntent()
        }
    }

    /**
     * Creates a URI for the image file and launches the camera intent to capture a photo.
     */
    private fun dispatchTakePictureIntent() {
        val context = fragment.requireContext()
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Create a file provider URI for the image
        val photoFile = createImageFile(context)

        currentPhotoUri = photoFile?.let {

            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it)

        }

        currentPhotoUri?.let { uri ->
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            // Grant temporary read permission to the camera app
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (takePictureIntent.resolveActivity(context.packageManager) != null) {
                takePictureLauncher.launch(uri)
            }
        }
    }

    /**
     * Dispatches an intent to pick an image from the gallery.
     */
    private fun dispatchPickImageIntent() {
        pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    /**
     * Processes the picked image URI:
     * - Corrects the orientation based on EXIF data.
     * - Converts the image to a circular shape.
     * - Sets the processed image to the ImageView.
     */
    private fun handleImage(uri: Uri) {
        Log.d("imageView11", "imageView11 $uri")
        imageUri = uri // Save the URI


        val context = fragment.requireContext()
        val correctedBitmap = getCorrectedBitmap(uri, context)
        val circularBitmap = getCircularBitmap(correctedBitmap)
       // imageView?.setImageBitmap(circularBitmap)

        // Compress and save image to a size below 1000 KB
        createAndSaveCompressedImage(context, imageUri!!, 1000 * 1024) // 1000 KB = 1000 * 1024 bytes
        getImageUri(imageUri!!)

        // Notify the callback with the image URI
        imageUriCallback.onImageUriReceived(uri)
    }

    fun getImageUri(imageUri: Uri): Uri {
        return imageUri
    }

    /**
     * Reads the image from the provided URI, corrects its orientation based on EXIF data.
     */
    private fun getCorrectedBitmap(uri: Uri, context: Context): Bitmap {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val exif = ExifInterface(context.contentResolver.openInputStream(uri)!!)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        // Determine rotation based on EXIF orientation
        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        return rotateBitmap(bitmap, rotationDegrees.toFloat())
    }

    /**
     * Rotates the given bitmap by the specified number of degrees.
     */
    private fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(rotationDegrees)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * Converts the given bitmap into a circular bitmap.
     */
    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        // Determine the size of the square bitmap
        val size = Math.min(bitmap.width, bitmap.height)
        // Crop the bitmap to a square shape
        val x = (bitmap.width - size) / 2
        val y = (bitmap.height - size) / 2
        val squaredBitmap = Bitmap.createBitmap(bitmap, x, y, size, size)

        // Create a new bitmap with a circular shape
        val outputBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBitmap)
        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)
        return outputBitmap
    }

    /**
     * Creates a temporary file to store the captured image.
     */
    private fun createImageFile(context: Context): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    /**
     * Compresses the bitmap and saves it to a file.
     * @param bitmap The bitmap to compress.
     * @param file The file to save the compressed bitmap.
     * @param maxFileSize The maximum file size in KB.
     * @throws IOException If an error occurs while saving the file.
     */
    private fun saveCompressedBitmap(bitmap: Bitmap, file: File, maxFileSize: Int = 1000) {
        var quality = 90
        var compressedBitmap: ByteArray? = null
        val outputStream = ByteArrayOutputStream()

        do {
            outputStream.reset() // Reset the stream for each compression attempt
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            compressedBitmap = outputStream.toByteArray()
            val currentFileSize = compressedBitmap.size / 1024 // Convert size to KB

            // If the image size is less than or equal to maxFileSize KB, stop compressing
            if (currentFileSize <= maxFileSize) {
                break
            }

            quality -= 5 // Reduce quality to reduce file size
        } while (quality > 0)

        // Save the compressed image to file
        FileOutputStream(file).use {
            it.write(compressedBitmap)
        }

        // Print the compressed image size
        val finalFileSize = file.length() / 1024 // Size in KB
        Log.d("ImageSize", "Compressed image size: ${finalFileSize}KB")
        println("Compressed image size: ${finalFileSize}KB")
    }

    /**
     * Creates and saves a compressed image to a file.
     * @param context The context to access external files.
     * @param imageUri The URI of the image to be compressed.
     * @param maxFileSize The maximum file size in bytes.
     */
    private fun createAndSaveCompressedImage(context: Context, imageUri: Uri, maxFileSize: Int): File? {
        val file = createImageFile(context) ?: return null
        val bitmap = getCorrectedBitmap(imageUri, context)

        try {
            saveCompressedBitmap(bitmap, file, maxFileSize / 1024) // Convert to KB
            // Get the file size in kilobytes (KB)
            val fileSizeInKB = file.length() / 1024
            Log.d("ImageSize", "Compressed image size: ${fileSizeInKB}KB")
            println("Compressed image size: ${fileSizeInKB}KB")  // Prints the size in console
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return file
    }

    fun getRealPathFromUri(imageUri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = fragment.requireContext().contentResolver.query(imageUri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return null
    }
}










