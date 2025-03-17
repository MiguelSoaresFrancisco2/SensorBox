package com.example.sensorbox

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sensorbox.audio.AudioRecorder
import com.example.sensorbox.temperature.BatteryHelper
import com.example.sensorbox.location.LocationHelper
import com.example.sensorbox.sensors.SensorManagerHelper
import com.example.sensorbox.sensorui.SensorBoxUI

class MainActivity : ComponentActivity() {

    private lateinit var sensorManagerHelper: SensorManagerHelper
    private lateinit var locationHelper: LocationHelper
    private lateinit var audioRecorder: AudioRecorder
    private lateinit var batteryHelper: BatteryHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManagerHelper = SensorManagerHelper(this)
        locationHelper = LocationHelper(this)
        audioRecorder = AudioRecorder(this)
        batteryHelper = BatteryHelper(this)

        setContent {
            SensorBoxUI(
                sensorValues = sensorManagerHelper.sensorValues,
                location = locationHelper.location,
                batteryTemp = batteryHelper.batteryTemp,
                soundLevel = audioRecorder.soundLevel
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManagerHelper.unregisterSensors()
        locationHelper.stopLocationUpdates()
        audioRecorder.stopRecording()
    }


}
