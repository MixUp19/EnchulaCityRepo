package com.example.emchulacity.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import androidx.core.net.toUri

class GalleryViewModel(context: Context) : ViewModel() {
    private val appContext = context.applicationContext
    private val _imageUrls = mutableStateListOf<String>()
    val imageUrls: List<String> get() = _imageUrls

    init {
        loadSavedImages(context)
    }

    fun loadSavedImages(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageUris = mutableListOf<Uri>()
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_ADDED
            )

            val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                "${MediaStore.Images.Media.RELATIVE_PATH} = ?"
            } else {
                "${MediaStore.Images.Media.DATA} LIKE ?"
            }

            val selectionArgs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf("Pictures/EnchulaCity/")
            } else {
                arrayOf("%EnchulaCity%")
            }

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            context.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    imageUris.add(contentUri)
                }
            }

            imageUris.map { _imageUrls.add(it.toString()) }
        }
    }

    fun deleteImage(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val uri = imageUrl.toUri()
            // Intenta borrar el archivo usando el content resolver
            val rowsDeleted = appContext.contentResolver.delete(uri, null, null)
            if (rowsDeleted > 0) {
                // Actualiza la lista en el hilo principal para que la UI se refresque
                withContext(Dispatchers.Main) {
                    _imageUrls.remove(imageUrl)
                }
            } 
        }
    }
}
//private fun fetchImages() {
//    viewModelScope.launch(Dispatchers.IO) {
//        val client = OkHttpClient()
//        repeat(3) {
//            val request = Request.Builder()
//                .url("https://dog.ceo/api/breeds/image/random")
//                .build()
//
//            client.newCall(request).execute().use { response ->
//                val responseData = response.body?.string()
//                responseData?.let {
//                    val json = JSONObject(it)
//                    val imageUrl = json.getString("message")
//                    _imageUrls.add(imageUrl)
//                }
//            }
//        }
//    }
//}