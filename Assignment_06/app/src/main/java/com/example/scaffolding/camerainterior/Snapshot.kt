package com.example.scaffolding.camerainterior

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Locale

fun takePhoto(
    context: Context,
    imageCapture: ImageCapture
) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null){
        Toast.makeText(
            context,
            "Log in first",
            Toast.LENGTH_SHORT
        ).show()
        return
    }


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
            ) {
                Log.e(
                    "CameraScreen", "Photo capture failed", exception
                )

                Toast.makeText(
                    context, "Couldn't capture", Toast.LENGTH_SHORT).show()
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val photoUri = output.savedUri
                if (photoUri == null) {
                    Toast.makeText(
                        context,
                        "Photo saved to your device, but no URI generated.",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                uploadPhoto(
                    photoUri = photoUri,
                    fileName = name,
                    onComplete = { success ->
                        val message = if (success) {
                            "Photo saved and uploaded"
                        } else {
                            "Photo saved but NOT uploaded"
                        }

                        Toast.makeText(
                            context, message, Toast.LENGTH_SHORT
                        ).show()
                    },
                    userId = user.uid
                )
            }
        }
    )
}

private fun uploadPhoto(
    photoUri: Uri,
    userId: String,
    fileName: String,
    onComplete: (Boolean) -> Unit
) {
    val storagePath = "users/$userId/$fileName.jpg"
    val storageRef = FirebaseStorage.getInstance().reference.child(storagePath)

    storageRef.putFile(photoUri).addOnSuccessListener {
        val photoData = hashMapOf(
            "ownerId" to userId,
            "fileName" to "$fileName.jpg",
            "storagePath" to storagePath,
            "createdAt" to FieldValue.serverTimestamp()
        )

        FirebaseFirestore.getInstance().collection("photos").add(photoData).addOnSuccessListener {
            onComplete(true)
        }
            .addOnFailureListener { exception ->
                Log.e("PhotoUpload", "Metadata save failed", exception)
                onComplete(false)
            }
    }
        .addOnFailureListener { exception ->
            Log.e(
                "PhotoUpload", "Image upload failed", exception
            )
            onComplete(false)
        }
}