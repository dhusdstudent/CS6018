package com.example.scaffolding.camerainterior

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Locale

fun takePhoto(
    context: Context,
    imageCapture: ImageCapture
) {
    val name = SimpleDateFormat(
        "yyyy-MM-dd-HH-mm-ss-SSS",
        Locale.US).format(System.currentTimeMillis())

    val contentVals = ContentValues().apply { put(
        MediaStore.Images.Media.DISPLAY_NAME, name)

        put (MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Scaffolding")
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentVals).build()

    imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {

            override fun onError(
                exception: ImageCaptureException
            ){
                Log.e(
                    "CameraScreen", "Photo capture failed", exception
                )
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Toast.makeText(
                    context, "Photo saved!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}