package com.example.emchulacity.nav

import android.Manifest
import android.widget.Toast
import androidx.compose.runtime.Composable
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.emchulacity.MainActivity
import androidx.core.app.ActivityCompat
import com.example.emchulacity.screens.LobbyScreen
import com.example.emchulacity.screens.CameraScreen
import com.example.emchulacity.screens.GalleryScreen


fun verificatePermission(
    requestPermission: ActivityResultLauncher<String>,
    navigate : () -> Unit
    ){
    val camera = Manifest.permission.CAMERA
    val context = LocalContext as MainActivity
    when{
        ContextCompat.checkSelfPermission(
            context,
            camera
        ) == PackageManager.PERMISSION_GRANTED -> {
                navigate()
        }
        ActivityCompat.shouldShowRequestPermissionRationale(
            context , camera) -> {
            Toast.makeText(context, "Necesitamos acceso a la cámara", Toast.LENGTH_LONG).show()
        }
        else -> {
            requestPermission.launch(
               camera
            )
        }
    }
}

@Composable
fun Navigation(
    requestPermission:  ActivityResultLauncher<String>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.lobby.name,
        modifier = modifier
    ) {
        composable(Screens.lobby.name) {
            LobbyScreen(
                cameraNavigate = { verificatePermission(
                    requestPermission,
                    { navController.navigate(Screens.camera.name) }
                ) },
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