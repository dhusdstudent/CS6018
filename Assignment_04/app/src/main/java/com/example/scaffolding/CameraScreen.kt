package com.example.scaffolding

import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraScreen(){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var surfaceAsk by remember {
        mutableStateOf<androidx.camera.core.SurfaceRequest?>()
    }

    var lensView by remember {
        mutableStateOf(CameraSelector.LENS_FACING_BACK)
    }

    val preview = remember { Preview.Builder().build().also {
        previewUseCase ->
            previewUseCase.setSurfaceProvider{
                request -> surfaceAsk = request
            }
        }
    }

    LaunchedEffect(lensView) {
        val cameraProvide = ProcessCameraProvider
            .getInstance(context).await()

        val cameraSelect = CameraSelector.Builder()
            .requireLensFacing(lensView).build()

        try {
            cameraProvide.unbindAll()
            cameraProvide.bindToLifecycle(
                lifecycleOwner,
                cameraSelect,
                preview
            )
        } catch (e:  Exception){
            Log.e("Camera Screen", "Binding failed", e)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        surfaceAsk?.let{ request ->
            CameraXViewfinder(
                surfaceAsk = request,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}