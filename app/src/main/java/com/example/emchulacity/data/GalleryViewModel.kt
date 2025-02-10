package com.example.emchulacity.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class GalleryViewModel : ViewModel() {
    private val _imageUrls = mutableStateListOf<String>()
    val imageUrls: List<String> get() = _imageUrls

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            repeat(20) {
                val request = Request.Builder()
                    .url("https://dog.ceo/api/breeds/image/random")
                    .build()

                client.newCall(request).execute().use { response ->
                    val responseData = response.body?.string()
                    responseData?.let {
                        val json = JSONObject(it)
                        val imageUrl = json.getString("message")
                        _imageUrls.add(imageUrl)
                    }
                }
            }
        }
    }
}
