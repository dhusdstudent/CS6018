package com.example.scaffolding.camerainterior

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.core.content.ContextCompat

fun Analysis(
    imageAnalysis: ImageAnalysis,
    context: Context,
    onPointFound: (Float, Float) -> Unit
) {

    imageAnalysis.setAnalyzer(
        ContextCompat.getMainExecutor(context)
    ) { image ->
        val plane = image.planes[0]
        val buffer = plane.buffer

        val width = image.width
        val height = image.height
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride

        var brightestValue = -1
        var brightestX = 0
        var brightestY = 0

        for (y in 0 until height){
            for (x in 0 until width){
                val index = y * rowStride + x * pixelStride
                val value = buffer.get(index).toInt() and 0xFF

                if (value> brightestValue){
                    brightestValue = value
                    brightestX = x
                    brightestY = y
                }
            }
        }

        val normalX = brightestX.toFloat() / width
        val normalY = brightestY.toFloat() / height

        onPointFound(
            normalX,
            normalY
        )

        image.close()
    }
}