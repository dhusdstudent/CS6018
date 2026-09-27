package com.example.scaffolding

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp


@Composable
fun CameraPreview(
    surfaceRequest: SurfaceRequest?
) {
    surfaceRequest?.let { request ->
        CameraXViewfinder(
            surfaceRequest = request,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun PointOfInterestOverlay(
    x: Float,
    y: Float
) {
    Canvas (
        modifier = Modifier.fillMaxSize()
    ) {
        val canvasX = x * size.width
        val canvasY = y * size.height

        drawCircle(
            color = Color.Blue,
            radius = 25f,
            center = Offset(canvasX, canvasY),
            style = Stroke(width = 5f)
        )
    }
}

@Composable
fun CameraControls(
    modifier: Modifier = Modifier,
    onSwitchCamera: () -> Unit,
    onTakePhoto: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(15.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SwitchCameraButton(
            onClick = onSwitchCamera
        )

        SavePhotoButton(
            onClick = onTakePhoto
        )
    }
}

@Composable
private fun SwitchCameraButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
        Text("Switch")
    }
}

@Composable
private fun SavePhotoButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
        Text ("Save Photo")
    }
}