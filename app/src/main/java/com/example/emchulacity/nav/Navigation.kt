package com.example.emchulacity.nav

import android.Manifest
import android.content.Context
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
import androidx.navigation.toRoute
import com.example.emchulacity.screens.LobbyScreen
import com.example.emchulacity.screens.CameraScreen
import com.example.emchulacity.screens.GalleryScreen


fun verificatePermission(
    requestPermission: ActivityResultLauncher<String>,
    context: Context,
    navigate : () -> Unit
    ){
    val camera = Manifest.permission.CAMERA
    val read = Manifest.permission.READ_EXTERNAL_STORAGE
    val write = Manifest.permission.WRITE_EXTERNAL_STORAGE
    var cameraIsGranted = false
    var readIsGranted  = false
    var writeIsGranted  = false
    val thiscontext = context
    when{
        ContextCompat.checkSelfPermission(
            thiscontext,
            camera
        ) == PackageManager.PERMISSION_GRANTED -> {
                cameraIsGranted = true
        }
        ActivityCompat.shouldShowRequestPermissionRationale(
            thiscontext as MainActivity , camera) -> {
            Toast.makeText(thiscontext, "Necesitamos acceso a la cámara", Toast.LENGTH_LONG).show()
        }
        else -> {
            requestPermission.launch(
               camera
            )
        }
    }
    when{
        ContextCompat.checkSelfPermission(
            thiscontext,
            write
        ) == PackageManager.PERMISSION_GRANTED -> {
            writeIsGranted = true
        }
        ActivityCompat.shouldShowRequestPermissionRationale(
            thiscontext as MainActivity , write) -> {
            Toast.makeText(thiscontext, "Necesitamos acceso a la cámara", Toast.LENGTH_LONG).show()
        }
        else -> {
            requestPermission.launch(
                write
            )
        }
    }
    when{
        ContextCompat.checkSelfPermission(
            thiscontext,
            read
        ) == PackageManager.PERMISSION_GRANTED -> {
            readIsGranted = true
        }
        ActivityCompat.shouldShowRequestPermissionRationale(
            thiscontext as MainActivity , read) -> {
            Toast.makeText(thiscontext, "Necesitamos acceso a la cámara", Toast.LENGTH_LONG).show()
        }
        else -> {
            requestPermission.launch(
                read
            )
        }
    }
    if (cameraIsGranted && readIsGranted && writeIsGranted){
        navigate()
    }else{
        Toast.makeText(thiscontext, "Necesitamos acceso a los permisos solicitados", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun Navigation(
    requestPermission:  ActivityResultLauncher<String>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screens.lobby.name,
        modifier = modifier
    ) {
        composable(Screens.lobby.name) {
            LobbyScreen(
                cameraNavigate = { verificatePermission(
                    requestPermission,
                    context
                ) { navController.navigate(Screens.camera.name) }
                },
                galleriesNavigate = { navController.navigate(Screens.gallery.name) }
            )
        }
        composable(Screens.camera.name) {
            CameraScreen(navController)
        }
        composable(Screens.gallery.name) {
            GalleryScreen()
        }
    }
}