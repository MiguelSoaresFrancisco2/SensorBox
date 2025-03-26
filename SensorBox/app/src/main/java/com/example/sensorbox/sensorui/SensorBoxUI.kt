package com.example.sensorbox.sensorui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun SensorBoxUI(
    sensorValues: Map<String, String>,
    location: String,
    batteryTemp: String,
    soundLevel: String,
    alerts: List<String>
) {
    val scrollState = rememberScrollState() // 👈 ADICIONADO

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sensor Box") },
                backgroundColor = MaterialTheme.colors.primarySurface
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text("📍 Localização Atual", style = MaterialTheme.typography.h6)
            Text(location, modifier = Modifier.padding(4.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Text("🌡️ Temperatura da Colmeia (Bateria)", style = MaterialTheme.typography.h6)
            Text(batteryTemp, modifier = Modifier.padding(4.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Text("🎤 Nível de Som", style = MaterialTheme.typography.h6)
            Text(soundLevel, modifier = Modifier.padding(4.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Text("📡 Valores dos Sensores em Tempo Real", style = MaterialTheme.typography.h6)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                items(sensorValues.keys.toList()) { sensorName ->
                    Text(
                        text = "$sensorName: ${sensorValues[sensorName]}",
                        modifier = Modifier.padding(4.dp)

                    )
                }
            }

            val gravityRaw = sensorValues["Gravidade"]
            val gravityComponents = gravityRaw
                ?.split(",")
                ?.map { it.trim().toDoubleOrNull() }

            val gravityX = gravityComponents?.getOrNull(0)?.let { String.format("%.2f", it) } ?: "-"
            val gravityY = gravityComponents?.getOrNull(1)?.let { String.format("%.2f", it) } ?: "-"
            val gravityZ = gravityComponents?.getOrNull(2)?.let { String.format("%.2f", it) } ?: "-"




            Spacer(modifier = Modifier.height(16.dp))

            Text("🛑 Estado da Colmeia", style = MaterialTheme.typography.h6)

            if (alerts.isEmpty()) {
                Text("Nenhum alerta no momento.", modifier = Modifier.padding(4.dp))
            } else {
                Column {
                    alerts.forEach { alert ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            backgroundColor = getAlertColor(alert),
                            elevation = 4.dp
                        ) {
                            Text(
                                text = alert,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Escolhe cor conforme o tipo de alerta
@Composable
fun getAlertColor(alert: String): Color {
    return when {
        alert.contains("✅") -> Color(0xFFDFF6DD) // verde claro
        alert.contains("⚠️") -> Color(0xFFFFF8DC) // amarelo claro
        alert.contains("🚨") || alert.contains("🐝") -> Color(0xFFFFDAD6) // vermelho claro
        else -> MaterialTheme.colors.surface
    }
}
