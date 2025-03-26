package com.example.sensorbox.model

enum class AlertType(val label: String) {
    SOUND("Estado Sonoro"),
    MOVEMENT("Movimento / Inclinação"),
    SILENCE("Atividade Sonora"),
    SWARM("Enxameação")
}
