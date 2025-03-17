package com.example.sensorbox.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import androidx.compose.runtime.*
import java.io.IOException

class AudioRecorder(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    var soundLevel by mutableStateOf("Obtendo nível de som...")

    init {
        requestMicrophonePermission()
    }

    private fun requestMicrophonePermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startRecording()
        } else {
            soundLevel = "Permissão negada para o microfone"
        }
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile("${context.externalCacheDir?.absolutePath}/sound.3gp")
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
                start()
            } catch (e: IOException) {
                soundLevel = "Erro ao medir som"
                return
            }
        }

        val soundUpdateThread = Thread {
            while (mediaRecorder != null) {
                val amplitude = mediaRecorder?.maxAmplitude ?: 0
                val db = if (amplitude > 0) (20 * kotlin.math.log10(amplitude.toDouble())).toInt() else 0
                soundLevel = "$db dB"
                Thread.sleep(500)
            }
        }

        soundUpdateThread.start()
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }
}
