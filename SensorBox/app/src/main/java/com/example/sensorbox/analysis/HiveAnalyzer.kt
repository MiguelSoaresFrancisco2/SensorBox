package com.example.sensorbox.analysis

object HiveAnalyzer {

    private const val BUFFER_SIZE = 10
    private const val ACC_BUFFER_SIZE = 50
    private const val ALERT_PERSISTENCE_SIZE = 40 // ~10 segundos

    private val lastDbValues = mutableListOf<Int>()
    private val lastAccMagnitudes = mutableListOf<Double>()
    private val accAlertHistory = mutableListOf<Boolean>()
    private val tiltAlertHistory = mutableListOf<Boolean>()

    fun analyze(
        sensorValues: Map<String, String>,
        soundLevel: String
    ): List<String> {
        val alerts = mutableListOf<String>()

        // 💡 Luminosidade
        val lux = sensorValues["Luminosidade"]
            ?.replace(",", ".")
            ?.trim()
            ?.toDoubleOrNull() ?: 0.0

        if (lux >= 25) {
            alerts.add("☀️ Alerta: Colmeia aberta! (Luminosidade: %.1f lux)".format(lux))
        }

        // 📦 Movimento
        val linAcc = parseSensor(sensorValues["Aceleração Linear"])
        val linAccMag = magnitude(linAcc)
        updateAccBuffer(linAccMag)
        val accFiltered = average(lastAccMagnitudes)

        val accAnormal = accFiltered > 2.5
        updateBooleanBuffer(accAlertHistory, accAnormal)

        if (accAlertHistory.count { it } >= 8) {
            alerts.add("🚨 Movimento suspeito na colmeia (Aceleração média: %.2f)".format(accFiltered))
        }

        // 📐 Inclinação (gravidade Z a partir de posição 4 e 5)
        val gravity = parseSensor(sensorValues["Gravidade"])
        val zInt = gravity.getOrNull(4) ?: 0.0
        val zDec = gravity.getOrNull(5) ?: 0.0
        val gravityZ = zInt + (zDec / 100)
        val isTilted = gravityZ < 8.0
        updateBooleanBuffer(tiltAlertHistory, isTilted)

        if (tiltAlertHistory.count { it } >= 8) {
            alerts.add("⚠️ Colmeia inclinada (Gravidade: %.2f)".format(gravityZ))
        }

        // 🔊 Som
        val db = soundLevel.filter { it.isDigit() }.toIntOrNull() ?: 0
        updateSoundBuffer(db)

        if (db < 25) {
            alerts.add("🔇 Baixa atividade sonora (Som: $db dB)")
        } else if (db in 66..80) {
            alerts.add("🔊 Agitação sonora (Som: $db dB)")
        }

        // 🐝 Enxameação
        if (shouldTriggerSwarmAlert()) {
            alerts.add("🐝 Alerta de possível enxameação! (>80 dB por vários segundos)")
        }

        return alerts
    }

    private fun updateAccBuffer(acc: Double) {
        if (lastAccMagnitudes.size >= ACC_BUFFER_SIZE) {
            lastAccMagnitudes.removeAt(0)
        }
        lastAccMagnitudes.add(acc)
    }

    private fun updateBooleanBuffer(buffer: MutableList<Boolean>, value: Boolean) {
        if (buffer.size >= ALERT_PERSISTENCE_SIZE) {
            buffer.removeAt(0)
        }
        buffer.add(value)
    }

    private fun average(values: List<Double>): Double {
        return if (values.isNotEmpty()) values.average() else 0.0
    }

    private fun updateSoundBuffer(db: Int) {
        if (lastDbValues.size >= BUFFER_SIZE) {
            lastDbValues.removeAt(0)
        }
        lastDbValues.add(db)
    }

    private fun shouldTriggerSwarmAlert(): Boolean {
        val aboveThreshold = lastDbValues.count { it > 80 }
        return aboveThreshold >= 8
    }

    private fun parseSensor(value: String?): List<Double> {
        return value?.split(",")?.mapNotNull { it.trim().toDoubleOrNull() } ?: emptyList()
    }

    private fun magnitude(values: List<Double>): Double {
        return Math.sqrt(values.sumOf { it * it })
    }
}
