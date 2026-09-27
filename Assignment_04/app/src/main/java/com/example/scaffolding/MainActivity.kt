package com.example.scaffolding

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private val camHasPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { itDoes ->
        if (itDoes){
            setContent {
                CameraScreen()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
                ) {
            setContent {
                CameraScreen()
            }
        }else {
            camHasPermission.launch(
                Manifest.permission.CAMERA
            )
        }
    }
}
