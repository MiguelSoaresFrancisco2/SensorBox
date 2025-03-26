package com.example.sensorbox.model

data class AlertStatus(
    val type: AlertType,
    val message: String,
    val level: AlertLevel
)

enum class AlertLevel {
    NORMAL,
    WARNING,
    ALERT
}
