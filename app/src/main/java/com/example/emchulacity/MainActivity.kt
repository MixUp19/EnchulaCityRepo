package com.example.emchulacity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.emchulacity.ui.theme.EmchulaCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmchulaCityTheme {
                EnchulaAPP()
            }
        }
    }

    fun requestPermission(){
       registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if(isGranted){
          // Permission granted
        }else{
          // Permission denied
        }
      }
    }
}
