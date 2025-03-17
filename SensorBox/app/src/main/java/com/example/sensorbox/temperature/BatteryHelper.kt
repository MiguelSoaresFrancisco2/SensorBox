package com.example.sensorbox.temperature

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.runtime.*

class BatteryHelper(context: Context) {

    var batteryTemp by mutableStateOf("Obtendo temperatura...")

    init {
        getBatteryTemperature(context)
    }

    private fun getBatteryTemperature(context: Context) {
        val batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val temp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)?.div(10.0)
                batteryTemp = if (temp != null) "%.1f°C".format(temp) else "Não disponível"
            }
        }

        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryReceiver, intentFilter)
    }
}
