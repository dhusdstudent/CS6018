package com.example.scaffolding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Login() {
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var errorMessage by remember {mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Scaffolding")

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            }
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            }
        )

        Button (
            onClick = {
                auth.signInWithEmailAndPassword(
                    email, password
                ).addOnCompleteListener { task ->

                    if (!task.isSuccessful) {
                        errorMessage = task.exception?.message
                            ?: "Login failed"
                    }
                }
            }
        ) {
            Text("Log In")
        }
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage)
        }
    }
}