package com.example.scaffolding


import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.scaffolding.camerainterior.Analysis
import com.example.scaffolding.camerainterior.DetectionModel
import com.example.scaffolding.camerainterior.takePhoto

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

        //Where lens is facing
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

        //Which model we're using
    var selectedModel by remember { mutableStateOf(DetectionModel.ML_KIT)}

        //what model detects
    var detections by remember {mutableStateOf<List<DetectObj>>(emptyList())}

    val preview = remember { Preview.Builder().build() }
    val imageCapture = remember { ImageCapture.Builder().build() }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder().setBackpressureStrategy(
            ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST //Ignores the ones that are older
        ).build()
    }

    LaunchedEffect(preview, previewView) {
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

//    LaunchedEffect(selectedModel) {
//        Analysis(
//            imageAnalysis = imageAnalysis,
//            context = context,
//            model = selectedModel,
//            previewView = previewView,
//            onDetectionsFound = { newDetections ->
//                detections = newDetections //recomposes here
//            }
//        )
//    }

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

    DisposableEffect(selectedModel) {
        val detector = Analysis(
            imageAnalysis = imageAnalysis,
            context = context,
            model = selectedModel,
            previewView = previewView,
            onDetectionsFound = { newDetections ->
                detections = newDetections
            }
        )

        onDispose {
            imageAnalysis.clearAnalyzer()
            detector.close()
        }
    }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CameraPreview(
                previewView = previewView,
                modifier = Modifier.fillMaxSize()
            )

            DetectionOverlay(
                detections = detections
            )

            CameraControls(
                modifier = Modifier.align(Alignment.BottomCenter),
                selectedModel = selectedModel,
                onModelSelected = {
                    selectedModel = it
                },

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
