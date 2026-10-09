package com.example.scaffolding

import com.google.firebase.auth.FirebaseAuth
import kotlin.getValue

object FBHelp {
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
}