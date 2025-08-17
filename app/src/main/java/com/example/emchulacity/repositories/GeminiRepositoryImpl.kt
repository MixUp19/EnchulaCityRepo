package com.example.emchulacity.repositories

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiRepositoryImpl {
      suspend fun generateTextFromTextAndImage(prompt: String, image: Bitmap): String = withContext(Dispatchers.IO) {
        val boundary = "Boundary-${System.currentTimeMillis()}"
        val url = URL("http://192.168.0.188:8000/analyze")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
        connection.doOutput = true

        val outputStream = DataOutputStream(connection.outputStream)

        // Imagen a bytes
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        // Parte de la imagen
        outputStream.writeBytes("--$boundary\r\n")
        outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"image.png\"\r\n")
        outputStream.writeBytes("Content-Type: image/png\r\n\r\n")
        outputStream.write(imageBytes)
        outputStream.writeBytes("\r\n")

        // Parte del prompt
        outputStream.writeBytes("--$boundary\r\n")
        outputStream.writeBytes("Content-Disposition: form-data; name=\"prompt\"\r\n\r\n")
        outputStream.writeBytes(prompt)
        outputStream.writeBytes("\r\n")

        outputStream.writeBytes("--$boundary--\r\n")
        outputStream.flush()
        outputStream.close()

        val responseCode = connection.responseCode
        val inputStream: InputStream = if (responseCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream
        }
        val response = inputStream.bufferedReader().use { it.readText() }
         return@withContext response
    }
}