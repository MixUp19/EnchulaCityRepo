package com.example.emchulacity.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.google.ar.core.Frame
import com.google.ar.core.exceptions.NotYetAvailableException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.Objects
import io.github.sceneview.ar.ARSceneView
import android.os.Handler
import android.os.HandlerThread
import android.view.PixelCopy


const val IMAGE_CAPTURE_HANDLER_NAME = "PixelCopier"

/**
 * Capture image from [ARSceneView] using [PixelCopy].
 *
 * @return Returns the [Bitmap] of view.
 */
fun ARSceneView.captureImage(): Bitmap {
    // Create a bitmap the size of the scene view.
    val bitmap = Bitmap.createBitmap(
        width, height,
        Bitmap.Config.ARGB_8888
    )

    // Create a handler thread to offload the processing of the image.
    val handlerThread = HandlerThread(IMAGE_CAPTURE_HANDLER_NAME)
    handlerThread.start()

    PixelCopy.request(this, bitmap, { copyResult ->
        if (copyResult == PixelCopy.SUCCESS) {
            print("Created bitmap from ARSceneView success.")
        } else {
            print("Failed to create bitmap from ARSceneView.")
        }
        handlerThread.quitSafely()
    }, Handler(handlerThread.looper))

    return bitmap
}


fun getCurrentFrameAsBitmap(frame: Frame): Bitmap? {
    var image: Image? = null
    for (i in 0..4) { // Retry up to 5 times
        try {
            image = frame.acquireCameraImage()
            break
        } catch (e: NotYetAvailableException) {
            Log.d("ARSceneView", "Image is not yet available. Retrying...", e)
            Thread.sleep(50) // Wait for 50ms before retrying
        }
    }

    if (image == null) {
        return null // Return null if the image is still not available
    }

    val planes: Array<Image.Plane> = image.planes
    val yBuffer: ByteBuffer = planes[0].buffer
    val uBuffer: ByteBuffer = planes[1].buffer
    val vBuffer: ByteBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    val uBytes = ByteArray(uSize)
    val vBytes = ByteArray(vSize)
    uBuffer.get(uBytes, 0, uSize)
    vBuffer.get(vBytes, 0, vSize)

    for (i in 0 until uSize) {
        nv21[ySize + i * 2] = vBytes[i]
        nv21[ySize + i * 2 + 1] = uBytes[i]
    }

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(android.graphics.Rect(0, 0, image.width, image.height), 100, out)
    val jpegData = out.toByteArray()

    val bitmap = android.graphics.BitmapFactory.decodeByteArray(jpegData, 0, jpegData.size)

    image.close()
    return bitmap.rotate()

}

fun Bitmap.rotate(value: Float = 90F) : Bitmap {
    val matrix = android.graphics.Matrix()
    matrix.postRotate(value)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun saveMediaToStorage(bitmap: Bitmap, context: Context, onUriCreated: (Uri) -> Unit) {
    var fos: OutputStream? = null

//Generating a file name
    val filename = "${System.currentTimeMillis()}.jpg"

    try {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$filename.jpg")

            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = Objects.requireNonNull(imageUri)?.let {
                resolver.openOutputStream(it)
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos!!)
            Objects.requireNonNull(fos)

            imageUri?.let { onUriCreated(it) }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
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
