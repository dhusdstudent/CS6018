package com.example.scaffolding

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login() {
    val auth = remember {FirebaseAuth.getInstance()}

    var email by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var errorMessage by remember {mutableStateOf("")}
    var isRegistering by remember {mutableStateOf(false)}
    var isLoading by remember {mutableStateOf(false )}

    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isRegistering) {
                "Create Account"
            } else {
                "Login"
            }
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
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

                if (email.isBlank() || password.isBlank()){
                    errorMessage = "Enter both an email and password"
                    return@Button
                }

                isLoading = true

                val operation = if (isRegistering) {
                    auth.createUserWithEmailAndPassword(
                        email.trim(),
                        password
                    )
                    } else {
                        auth.signInWithEmailAndPassword(
                            email.trim(),
                            password
                        )
                    }

                    operation.addOnCompleteListener { task ->
                        isLoading = false

                        if (!task.isSuccessful) {
                            errorMessage =
                                task.exception?.localizedMessage
                                    ?: "Authentication failed"
                        }
                    }
            },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                    ) {

            Text(
                if (isLoading) "Please wait"
                else if (isRegistering) "Create account"
                else "Log in"
            )
        }

        TextButton(
            onClick = {
                isRegistering = !isRegistering
                errorMessage = ""
            }
        ) {
            Text(
                if (isRegistering) {
                    "Already have an account? Log in"
                } else {
                    "New here? Create an account"
                }
            )
        }

        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}