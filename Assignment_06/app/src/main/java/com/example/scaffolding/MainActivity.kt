package com.example.scaffolding

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }


@Composable
fun App() {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    if (user == null) {
        LoginScreen()
    } else {
        CameraScreen()
    }
}

    private fun checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            setContent {
                CameraScreen()
            }
        } else {
            camHasPermission.launch(Manifest.permission.CAMERA)
        }
    }
}
