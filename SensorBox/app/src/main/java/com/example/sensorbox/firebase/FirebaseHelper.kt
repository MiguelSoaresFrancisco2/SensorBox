package com.example.sensorbox.firebase

import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {
    private val database = FirebaseDatabase.getInstance()
    private val colmeiaRef = database.getReference("colmeias/colmeia1")

    fun enviarDadosSensor(
        temperatura: String,
        localizacao: String,
        som: String,
        sensores: Map<String, String>,
        alertas: List<String>
    ) {
        val dados = mapOf(
            "temperatura" to temperatura,
            "localizacao" to localizacao,
            "nivelSom" to som,
            "sensores" to sensores,
            "alertas" to alertas
        )

        colmeiaRef.setValue(dados)
    }
}
