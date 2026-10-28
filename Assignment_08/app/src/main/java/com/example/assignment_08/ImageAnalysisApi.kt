package com.example.assignment_08

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

private const val IMAGE_ANALYSIS_URL = "http://10.0.2.2:8080/api/image-analysis"

suspend fun analyzeImage(
    imageBytes: ByteArray,
    idToken: String
) : String = withContext(Dispatchers.IO){
    val boundary = "Boundary-${UUID.randomUUID()}"
    val connection = URL(IMAGE_ANALYSIS_URL)
        .openConnection() as HttpURLConnection

    try {
        connection.requestMethod = "POST"
        connection.connectTimeout = 15000
        connection.readTimeout = 12000
        connection.doOutput = true

        connection.setRequestProperty(
            "Authorization",
            "Bearer $idToken"
        )
        connection.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=$boundary"
        )
        connection.setRequestProperty(
            "Accept",
            "application/json"
        )

        val body = ByteArrayOutputStream()

        body.write("--$boundary\r\n".toByteArray())
        body.write(
            "Content-Disposition: form-data; name=\"file\";filename=\"photo.jpg\"\r\n".toByteArray()
        )
        body.write("Content-Type: image/jpeg\r\n\r\n".toByteArray())
        body.write(imageBytes)
        body.write("\r\n--$boundary--\r\n".toByteArray())

        connection.getOutputStream().use{output ->
            body.writeTo(output)
        }

        val status = connection.responseCode
        val responseStream =
            if (status in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

        val responseText = responseStream
            ?.bufferedReader()
            ?.use { it.readText()}
            ?: throw IllegalStateException(
                "Server returned no response"
            )

        if (status !in 200..299) {
            val message = try {
                JSONObject(responseText).optString(
                    "error",
                    "Image analysis failed"
                )
            } catch (_: Exception) {
                "Image anaylsis failed (HTTP $status)"
            }
            throw IllegalStateException(message)
        }

        JSONObject(responseText).getString("caption")
    } finally {
        connection.disconnect()
    }
}