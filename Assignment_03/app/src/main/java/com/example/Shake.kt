package com.example.assignment_03

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import kotlin.math.abs
import kotlin.math.sqrt

data class Shake (
    val timestamp: Long,
    val vigor: Float
)
