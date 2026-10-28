package com.example.assignment_08

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private const val LOGIN_URL = "http://10.0.2.2:8080/login"

suspend fun requestCustomToken(
    username: String,
    password: String
) : String = withContext(Dispatchers.IO){

    val connection = URL(LOGIN_URL)
        .openConnection() as HttpURLConnection

    try {
        connection.requestMethod = "POST"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.doOutput = true

        connection.setRequestProperty(
            "Content-Type",
            "application/json"
        )

        connection.setRequestProperty(
            "Accept",
            "application/json"
        )

        val requestJson = JSONObject()
            .put("username", username)
            .put("password", password)
            .toString()

        connection.outputStream.use {
                output ->
            output.write(requestJson.toByteArray(Charsets.UTF_8))
        }

        val statusCode = connection.responseCode
        val responseStream = if (statusCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream
        }

        val responseText = responseStream
            ?.bufferedReader()
            ?.use { it.readText()}
            ?: throw IllegalStateException(
                "Authentication server returned no response"
            )

        val responseJson = JSONObject(responseText)

        if (statusCode !in 200..299){
            throw IllegalStateException(
                responseJson.optString(
                    "message",
                    "Login failed"
                )
            )
        }

        if (!responseJson.optBoolean("success")){
            throw IllegalStateException("Login failed")
        }

        responseJson.optString("customToken")
            .takeIf { it.isNotBlank()}
            ?: throw IllegalStateException(
                "Auth server no token"
            )
    } finally {
        connection.disconnect()
    }
}