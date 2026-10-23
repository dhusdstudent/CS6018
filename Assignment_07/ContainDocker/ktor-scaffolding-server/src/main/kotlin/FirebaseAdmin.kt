package com.example

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth

object FirebaseAdmin {

    init {
        val options = FirebaseOptions.builder().setCredentials(
            GoogleCredentials.getApplicationDefault()
        ).build()

        if (FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options)
        }
    }

    val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
}