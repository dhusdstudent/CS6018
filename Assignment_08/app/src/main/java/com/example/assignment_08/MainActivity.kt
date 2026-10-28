package com.example.assignment_08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }


    @Composable
    fun App() {
        val auth = remember { FirebaseAuth.getInstance() }

        var user by remember {
            mutableStateOf(auth.currentUser)
        }

        DisposableEffect(auth) {
            val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                user = firebaseAuth.currentUser
            }
            auth.addAuthStateListener(listener)

            onDispose {
                auth.removeAuthStateListener(listener)
            }
        }

        if (user == null) {
            Login()
        } else {
            var showGallerby by remember(user?.uid) {
                mutableStateOf(false)
            }

            if (showGallerby) {
                PhotoGalleryScreen(
                    onBack = { showGallerby = false },
                    onLogout = { auth.signOut() }
                )
            } else {
                CameraScreen(
                    onOpenGallery = { showGallerby = true },
                    onLogout = { auth.signOut() }
                )
            }
        }
    }
}
