package com.example.assignment_03

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.sqrt

class ShakeOmeter(
    private val onShake: (Shake) -> Unit
) : SensorEventListener {
    var lastShake = 0L
    var cooldown = 300L
    val threshold = 3.0f

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val vigor = sqrt((x * x) + (y * y) + (z * z))
        val movement = abs(vigor - SensorManager.GRAVITY_EARTH)

        val now = System.currentTimeMillis()

        if (movement > threshold && now - lastShake > cooldown) {
            val shake = Shake(
                timestamp = now,
                vigor = movement
            )

            lastShake = now
            onShake(shake)
        }
    }


    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ){}
}