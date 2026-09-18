package com.example.assignment_03

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun SnowGlobe() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        drawCircle(
            color = Color(0xFFBDEBFF),
            radius = 140f,
            center = center
        )

        drawCircle(
            color = Color.Black,
            radius = 140f,
            center = center,
            style = Stroke(width = 6f)
        )

        drawRect(
            color = Color(0xFF6D4C41),
            topLeft = Offset(70f, 260f),
            size = Size(160f, 60f)
        )
        //SNOW

        drawCircle(
            color = Color.White,
            radius = 5f,
            center = Offset(100f, 200f)
        )

        drawCircle(
            color = Color.White,
            radius = 5f,
            center = Offset(150f, 250f)
        )

        drawCircle(
            color = Color.White,
            radius = 5f,
            center = Offset(200f, 180f)
        )
    }
}
