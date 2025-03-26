package com.example.sensorbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.sensorbox.audio.AudioRecorder
import com.example.sensorbox.location.LocationHelper
import com.example.sensorbox.sensors.SensorManagerHelper
import com.example.sensorbox.sensorui.SensorBoxUI
import com.example.sensorbox.temperature.BatteryHelper
import com.example.sensorbox.analysis.HiveAnalyzer
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private lateinit var sensorManagerHelper: SensorManagerHelper
    private lateinit var locationHelper: LocationHelper
    private lateinit var audioRecorder: AudioRecorder
    private lateinit var batteryHelper: BatteryHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa os sensores e helpers
        sensorManagerHelper = SensorManagerHelper(this)
        locationHelper = LocationHelper(this)
        audioRecorder = AudioRecorder(this)
        batteryHelper = BatteryHelper(this)

        setContent {
            var alerts by remember { mutableStateOf(listOf<String>()) }

            // Atualiza os alertas continuamente a cada 250ms
            LaunchedEffect(Unit) {
                while (true) {
                    alerts = HiveAnalyzer.analyze(
                        sensorManagerHelper.sensorValues,
                        audioRecorder.soundLevel
                    )
                    delay(250)
                }
            }

            // UI principal
            SensorBoxUI(
                sensorValues = sensorManagerHelper.sensorValues,
                location = locationHelper.location,
                batteryTemp = batteryHelper.batteryTemp,
                soundLevel = audioRecorder.soundLevel,
                alerts = alerts
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManagerHelper.unregisterSensors()
        locationHelper.stopLocationUpdates()
    }
}
