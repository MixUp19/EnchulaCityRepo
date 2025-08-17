package com.example.emchulacity.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.emchulacity.data.GalleryViewModel
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.emchulacity.repositories.GeminiRepositoryImpl
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

val gemini = GeminiRepositoryImpl()
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GalleryScreen(viewModel: GalleryViewModel = GalleryViewModel(context = LocalContext.current)) {
    val imageUrls by remember { mutableStateOf(viewModel.imageUrls) }
    var selectedImage by remember { mutableStateOf<String?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(imageUrls) { imageUrl ->
            GlideImage(
                model = imageUrl,
                contentDescription = "gallery image",
                modifier = Modifier
                    .size(120.dp)
                    .padding(3.dp)
                    .clickable { selectedImage = imageUrl },
                contentScale = ContentScale.Crop
            )
        }
    }

    selectedImage?.let { imageUrl ->
        ZoomedImageDialog(imageUrl,
            onClose = { selectedImage = null },
            onDelete = {
                viewModel.deleteImage(imageUrl)
                selectedImage = null
            })
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ZoomedImageDialog(imageUrl: String, onClose: () -> Unit, onDelete: () -> Unit) {
    val context = LocalContext.current
    var showGeminiModal by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onClose) {
        Box(
            modifier = Modifier
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = "Zoomed Image",
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onClose() },
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    IconButton(onClick = { shareImage(context, imageUrl) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Black)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                    IconButton(onClick = { showGeminiModal = true }) {
                        Icon(Icons.Default.Star, contentDescription = "Request", tint = Color.hsl(51.0f,1.0f,.5f))
                    }
                }
            }
        }
    }

    if (showGeminiModal) {
        TestGeminiPromtpModal(
            onClose = { showGeminiModal = false },
            imageUrl = imageUrl
        )
    }
}

@Composable
fun TestGeminiPromtpModal(onClose: () -> Unit, imageUrl: String) {
    var ciudad by remember { mutableStateOf("") }
    var detalles by remember { mutableStateOf("") }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var result by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(imageUrl) {
        bitmap = withContext(Dispatchers.IO) {
            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .submit()
                .get()
        }
    }
    val prompt = "¿Qué añadiría en la imagen teniendo encuenta el clima de la ciudad de $ciudad? considerando: $detalles"

    Dialog(onDismissRequest = onClose) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                TextField(
                    onValueChange = { ciudad = it },
                    value = ciudad,
                    label = { Text("Ciudad") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    onValueChange = { detalles = it },
                    value = detalles,
                    label = { Text("Detalles") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        isLoading = true
                        showResultDialog = false
                        result = ""
                        // Lanza la corrutina para la petición
                        runBlocking{
                            result = gemini.generateTextFromTextAndImage(prompt, bitmap?: return@runBlocking)
                            isLoading = false
                            showResultDialog = true
                        }
                    },
                    enabled = !isLoading
                ) {
                    Text("Submit")
                }
            }
        }
    }
    if (isLoading) {
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressIndicator()
    }
    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { showResultDialog = false },
            confirmButton = {
                Button(onClick = { showResultDialog = false }) {
                    Text("Cerrar")
                }
            },
            title = { Text("Resultado") },
            text = {
                Column(
                    modifier = Modifier
                        .height(400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(AnnotatedString.fromHtml(result))
                }
            }
        )
    }
}



fun shareImage(context: Context, imageUrl: String) {
    val imageUri = imageUrl.toUri()
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share Image"))
}
