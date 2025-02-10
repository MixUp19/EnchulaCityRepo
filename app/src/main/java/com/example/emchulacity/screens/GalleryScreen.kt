package com.example.emchulacity.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.emchulacity.data.GalleryViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GalleryScreen(viewModel: GalleryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val imageUrls by remember { mutableStateOf(viewModel.imageUrls) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(imageUrls) { imageUrl ->
            GlideImage(
                model = imageUrl,
                contentDescription = "Dog Image",
                modifier = Modifier.size(120.dp)
                    .padding(3.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewGalleryScreen() {
    GalleryScreen()
}
