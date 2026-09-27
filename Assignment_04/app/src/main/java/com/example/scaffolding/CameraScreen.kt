package com.example.scaffolding


import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.scaffolding.camerainterior.Analysis
import com.example.scaffolding.camerainterior.takePhoto

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var surfaceRequest by remember { mutableStateOf<androidx.camera.core.SurfaceRequest?>(null) }

    var pointX by remember { mutableFloatStateOf(0.5f) }
    var pointY by remember { mutableFloatStateOf(0.5f) }

    val preview = remember { Preview.Builder().build() }
    val imageCapture = remember { ImageCapture.Builder().build() }

    val imageAnalysis = remember {
        ImageAnalysis.Builder().setBackpressureStrategy(
            ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        ).build()
    }

    LaunchedEffect(preview) {
        preview.setSurfaceProvider { request ->
            surfaceRequest = request
        }
    }
        Analysis(
            imageAnalysis = imageAnalysis,
            context = context,
            onPointFound = { x, y ->
                pointX = x
                pointY = y
            }
        )

        LaunchedEffect(lensFacing) {
            val cameraProvider = ProcessCameraProvider.getInstance(context).get()
            val cameraSelect = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelect,
                    preview,
                    imageCapture,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("Camera Screen", "Binding failed", e)
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CameraPreview(
                surfaceRequest = surfaceRequest
            )

            PointOfInterestOverlay(
                x = pointX,
                y = pointY
            )

            CameraControls(
                modifier = Modifier.align(Alignment.BottomCenter),
                onSwitchCamera = {
                    lensFacing =
                        if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
                },
                onTakePhoto = {
                    takePhoto(
                        context = context,
                        imageCapture = imageCapture
                    )
                }
            )

    }
}
