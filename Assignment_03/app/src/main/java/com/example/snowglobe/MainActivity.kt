package com.example.snowglobe

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import com.example.snowglobe.ui.theme.SnowGlobeTheme

class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var shakeOmeter: ShakeOmeter
    private lateinit var shakeLog: ShakeLog

    private val shakes = mutableStateListOf<Shake>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return
        shakeLog = ShakeLog(this)

        shakes.addAll(shakeLog.getShakes())
        shakeOmeter = ShakeOmeter { shake ->
            shakes.add(shake)
            shakeLog.saveShakes(shakes)
        }
        setContent {
            SnowGlobeTheme {
                MaracaScreen(
                    shakes = shakes,
                    onDeleteOldShakes = { cutoff ->
                        shakeLog.deleteOldOnes(cutoff)
                        shakes.clear()
                        shakes.addAll(shakeLog.getShakes())
                    })
            }
        }
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(
            shakeOmeter, accelerometer, SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeOmeter)
    }
}
