package com.example.assignment_03

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.min
@Composable
fun SnowGlobe(
    shakes: List<Shake>,
    modifier: Modifier = Modifier
){
    Canvas(
        modifier = modifier
    ){
        val globeAcross = min(size.width, size.height) * 0.35f
        val globeMiddle = Offset(
            x = size.width/2,
            y = size.height * 0.42f
        )

        drawCircle(
            color = Color(0xFFBDEBFF),
            radius = globeAcross,
            center = globeMiddle
        )

        drawCircle(
            color = Color.Black,
            radius = globeAcross,
            center = globeMiddle,
            style = Stroke(width = 5f)
        )

        drawRect(
            color = Color(0xFF6D4C41),
            topLeft = Offset(
                x = globeMiddle.x - 50f,
                y = globeMiddle.y + globeAcross - 5f
            ),
            size = Size(
                width = 150f,
                height = 50f
            )
        )

        //-------------------SNOWFLAKES!--------------------

        shakes.forEachIndexed { index, shake ->

            val x = globeMiddle.x + ((index * 70) % 120 - 60)
            val y = globeMiddle.y + ((index * 90) % 140 - 70)
            val radius = (3f + shake.vigor * 15f).coerceIn(3f, 14f)

            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(x,y)
            )

        }
    }
}