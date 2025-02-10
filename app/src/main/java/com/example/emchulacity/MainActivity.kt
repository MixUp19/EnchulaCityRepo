package com.example.emchulacity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.emchulacity.ui.theme.EmchulaCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmchulaCityTheme {
                EnchulaAPP(result = requestPermissionLauncher)
            }
        }
    }

    private val requestPermissionLauncher =
       registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted:Boolean ->
        if(isGranted){
          Toast.makeText(this, "Enchula tu ciudad", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Necesitamos Permiso para que disfrutes la experiencia", Toast.LENGTH_LONG).show()
        }
      }

}
