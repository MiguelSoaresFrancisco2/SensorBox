package com.example.sensorbox.sensorui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SensorBoxUI(
    sensorValues: Map<String, String>,
    location: String,
    batteryTemp: String,
    soundLevel: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sensor Box") },
                backgroundColor = MaterialTheme.colors.primarySurface
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
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
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(sensorValues.keys.toList()) { sensorName ->
                    Text(text = "$sensorName: ${sensorValues[sensorName]}", modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}
