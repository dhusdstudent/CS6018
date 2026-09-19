package com.example.undergraduateapp_phase2

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val department: String,
    val number: String
)