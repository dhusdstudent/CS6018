package com.example.scaffolding

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.scaffolding.camerainterior.DetectionModel


@Composable
fun CameraPreview(
    previewView: PreviewView,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = {previewView},
        modifier = modifier
    )
}


@Composable
fun CameraControls(
    modifier: Modifier = Modifier,
    selectedModel: DetectionModel,
    onModelSelected: (DetectionModel) -> Unit,
    onSwitchCamera: () -> Unit,
    onTakePhoto: () -> Unit
) {

    Column(modifier = modifier.fillMaxWidth().padding(10.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button( onClick = {
                onModelSelected(DetectionModel.ML_KIT)
            }) {
                Text(
                    if (selectedModel == DetectionModel.ML_KIT)
                        "√ ML Kit"
                    else
                        "ML Kit"
                )
            }

            Button (onClick = {
                onModelSelected(DetectionModel.EFFICIENT_DET)
            }) {
                Text(
                    if (selectedModel == DetectionModel.EFFICIENT_DET)
                        "√ EF Det"
                    else
                        "EF Det"
                )
            }
        }

        Row( //combining into structure
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button (
                onClick = onSwitchCamera
            ) {
                Text ("Flip Camera")
            }

            Button (
                onClick = onTakePhoto
            ) {
                Text ("Save Photo")
            }
        }
    }
}

@Composable
fun DetectionOverlay(
    detections: List<DetectObj>
) {
    val density = LocalDensity.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            for (detection in detections) {
                val box = detection.theBox
//TO DO
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(box.left, box.top),
                    size = Size(box.width, box.height),
                    style = Stroke(width = 5f)
                )
            }
        }

        detections.forEach { detection ->
            val box = detection.theBox

            Text(
                text = "${detection.label} ${detection.accuracy * 100}% ",
                color = Color.White,
                modifier = Modifier.offset(
                    x = with(density){
                        box.left.toDp()
                    },
                    y = with(density) {
                        box.top.toDp()
                    }
                ).background(Color.Black).padding(5.dp)
            )
        }
    }
}