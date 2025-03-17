package com.example.sensorbox.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.mutableStateMapOf

class SensorManagerHelper(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensorValues = mutableStateMapOf<String, String>()

    init {
        val sensorTypes = listOf(
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_MAGNETIC_FIELD,
            Sensor.TYPE_LIGHT,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_LINEAR_ACCELERATION,
            Sensor.TYPE_ROTATION_VECTOR
        )

        sensorTypes.forEach { type ->
            sensorManager.getDefaultSensor(type)?.let { sensor ->
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val sensorName = getSensorName(it.sensor.type)
            val formattedValues = it.values.joinToString(", ") { value ->
                String.format("%.2f", value)
            }

            sensorValues[sensorName] = formattedValues
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun unregisterSensors() {
        sensorManager.unregisterListener(this)
    }

    private fun getSensorName(sensorType: Int): String {
        return when (sensorType) {
            Sensor.TYPE_ACCELEROMETER -> "Acelerômetro"
            Sensor.TYPE_MAGNETIC_FIELD -> "Magnetômetro"
            Sensor.TYPE_LIGHT -> "Luminosidade"
            Sensor.TYPE_GRAVITY -> "Gravidade"
            Sensor.TYPE_GYROSCOPE -> "Giroscópio"
            Sensor.TYPE_LINEAR_ACCELERATION -> "Aceleração Linear"
            Sensor.TYPE_ROTATION_VECTOR -> "Vetor de Rotação"
            else -> "Sensor Desconhecido"
        }
    }
}
