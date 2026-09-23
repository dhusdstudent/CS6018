package com.example.scaffolding

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.scaffolding.ui.theme.ScaffoldingTheme

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
