package com.example.emchulacity.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.Log
import android.view.PixelCopy
import io.github.sceneview.ar.ARSceneView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Objects


/**
 * Capture image from [ARSceneView] using [PixelCopy].
 *
 * @return Returns the [Bitmap] of view.
 */
fun ARSceneView.captureImage(callback: (Bitmap?) -> Unit) {
    if (!isAttachedToWindow || width <= 0 || height <= 0) {
        callback(null)
        return
    }
    // Create a bitmap the size of the scene view.
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    // Create a handler thread to offload the processing of the image.
    val handlerThread = HandlerThread("IMAGE_CAPTURE_HANDLER").apply { start() }

    PixelCopy.request(
        this,
        bitmap,
        { copyResult ->
            if (copyResult == PixelCopy.SUCCESS) {
                // Notify the caller that the bitmap was successfully created.
                callback(bitmap)
            } else {
                // Notify the caller that the bitmap could not be captured.
                callback(null)
            }
            handlerThread.quitSafely()
        },
        Handler(handlerThread.looper)
    )
}


fun saveMediaToStorage(bitmap: Bitmap, context: Context, onUriCreated: (Uri) -> Unit) {
    val fos: OutputStream?
    // Generar un nombre de archivo
    val filename = "${System.currentTimeMillis()}.jpg"

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$filename.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                // Especifica la carpeta personalizada dentro de Pictures
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/EnchulaCity")
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos!!)
            fos.close()
            imageUri?.let { onUriCreated(it) }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val myDir = File(imagesDir, "EnchulaCity")
            if (!myDir.exists()) {
                myDir.mkdirs()
            }
            val image = File(myDir, filename)
            fos = FileOutputStream(image)

            fos.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }

            onUriCreated(Uri.fromFile(image))
        }
    } catch (e: Exception) {
        Log.d("error", e.toString())
    }
}

