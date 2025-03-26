package com.example.sensorbox.model

data class HiveSensorData(
    val timestamp: Long = System.currentTimeMillis(),
    val location: String = "",
    val hiveTemperature: String = "",
    val soundLevel: String = "",
    val lightLevel: String = "",
    val motion: Map<String, String> = emptyMap(),
    val alerts: List<String> = emptyList()
)
