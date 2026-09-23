package com.example.ams2mp08_guessnumber_rojasm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var numeroSecreto: Int = 0
    private var intentos: Int = 0

    private lateinit var tvIntentos: TextView
    private lateinit var etNumero: EditText
    private lateinit var btnAdivinar: Button
    private lateinit var tvHistorial: TextView
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvIntentos = findViewById(R.id.tvIntentos)
        etNumero = findViewById(R.id.etNumero)
        btnAdivinar = findViewById(R.id.btnAdivinar)
        tvHistorial = findViewById(R.id.tvHistorial)
        scrollView = findViewById(R.id.scrollView)

        nuevoJuego()

        btnAdivinar.setOnClickListener {
            procesarIntento()
        }
    }

    private fun nuevoJuego() {
        numeroSecreto = Random.nextInt(1, 101)
        intentos = 0

        tvIntentos.text = "Intentos: 0"
        tvHistorial.text = "--- Historial de intentos ---\n"

        etNumero.text.clear()
        etNumero.requestFocus()
    }

    private fun procesarIntento() {
        val entradaText = etNumero.text.toString()

        if (entradaText.isEmpty()) {
            Toast.makeText(
                this,
                "Por favor, introduce un número",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val numeroUsuario = entradaText.toInt()

        if (numeroUsuario < 1 || numeroUsuario > 100) {
            Toast.makeText(
                this,
                "El número debe estar entre 1 y 100",
                Toast.LENGTH_SHORT
            ).show()
            etNumero.text.clear()
            etNumero.requestFocus()
            return
        }

        intentos++

        tvIntentos.text = "Intentos: $intentos"

        if (numeroUsuario < numeroSecreto) {

            Toast.makeText(
                this,
                "El número secreto es MAYOR",
                Toast.LENGTH_SHORT
            ).show()

            agregarAlHistorial(
                "Intento $intentos: $numeroUsuario -> El número es MAYOR"
            )

        } else if (numeroUsuario > numeroSecreto) {

            Toast.makeText(
                this,
                "El número secreto es MENOR",
                Toast.LENGTH_SHORT
            ).show()

            agregarAlHistorial(
                "Intento $intentos: $numeroUsuario -> El número es MENOR"
            )

        } else {

            agregarAlHistorial(
                "Intento $intentos: $numeroUsuario -> ¡CORRECTO!"
            )

            Toast.makeText(
                this,
                "¡Correcto! Has acertado en $intentos intentos",
                Toast.LENGTH_LONG
            ).show()

            nuevoJuego()
        }

        etNumero.text.clear()
        etNumero.requestFocus()
    }

    private fun agregarAlHistorial(mensaje: String) {
        tvHistorial.append("$mensaje\n")

        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}