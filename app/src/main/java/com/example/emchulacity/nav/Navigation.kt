package com.example.emchulacity.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.emchulacity.screens.LobbyScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.lobby.name,
        modifier = modifier
    ) {
        composable(Screens.lobby.name) {
            LobbyScreen()
        }
        composable(Screens.camera.name) {
            //CameraScreen()
        }
        composable(Screens.gallery.name) {
            //GalleryScreen()
        }
    }
}