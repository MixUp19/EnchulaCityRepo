package com.example.emchulacity.nav

import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.emchulacity.EnchulaAPP
import com.example.emchulacity.EnchulaTopBar
import com.example.emchulacity.MainActivity
import com.example.emchulacity.screens.LobbyScreen
import com.example.emchulacity.screens.CameraScreen
import com.example.emchulacity.screens.GalleryScreen



@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.lobby.name,
        modifier = modifier
    ) {
        composable(Screens.lobby.name) {
            val context = LocalContext.current

            if (context is MainActivity) {
                context.requestPermission()
            } else {
                println("Context is not MainActivity")
            }

            LobbyScreen(
                cameraNavigate = { navController.navigate(Screens.camera.name) },
                galleriesNavigate = { navController.navigate(Screens.gallery.name) }
            )
        }
        composable(Screens.camera.name) {
            CameraScreen()
        }
        composable(Screens.gallery.name) {
            GalleryScreen()
        }
    }
}