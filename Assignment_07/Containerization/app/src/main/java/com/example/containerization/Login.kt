package com.example.containerization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun Login() {
    val auth = remember {FirebaseAuth.getInstance()}

    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var errorMessage by remember {mutableStateOf("")}
    //var isRegistering by remember {mutableStateOf(false)}
    var isLoading by remember {mutableStateOf(false )}

    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login")
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(15.dp))

        Button (
            onClick = {
                errorMessage = ""

                if (username.isBlank() || password.isBlank()) {
                    errorMessage = "Enter BOTH a username and password"
                    return@Button
                }

                isLoading = true

                scope.launch {
                    try {
                        val customToken = requestCustomToken(
                            username = username.trim(),
                            password = password
                        )

                        auth.signInWithCustomToken(customToken).await()
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Can't authenticate"
                    } finally {
                        isLoading = false
                    }
                }

            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth() ) {



            Text(
                if (isLoading) "Please wait..."
                else "Log in"
            )
        }

        if (errorMessage.isNotBlank()) {
            Spacer(Modifier.height(10.dp))

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}