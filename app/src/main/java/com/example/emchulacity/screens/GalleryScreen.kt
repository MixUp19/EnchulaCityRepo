package com.example.emchulacity.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.emchulacity.data.GalleryViewModel

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
                }
            }
        }
    }
}

fun shareImage(context: Context, imageUrl: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, imageUrl)
    }
    context.startActivity(Intent.createChooser(intent, "Share Image"))
}
