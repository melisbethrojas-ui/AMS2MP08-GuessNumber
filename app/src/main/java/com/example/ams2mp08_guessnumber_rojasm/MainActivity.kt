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
import android.text.InputType

class Record(
    val nombre: String,
    val intentos: Int
)

class MainActivity : AppCompatActivity() {

    var numeroSecreto: Int = 0
    var intentos: Int = 0
    var nombreJugador: String = ""
    private lateinit var tvIntentos: TextView
    private lateinit var etNumero: EditText
    private lateinit var btnAdivinar: Button
    private lateinit var tvHistorial: TextView
    private lateinit var scrollView: ScrollView
    private val clasificacion = mutableListOf(
        Record("Ana", 3),
        Record("Juan", 5),
        Record("Marta", 8)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvIntentos = findViewById(R.id.tvIntentos)
        etNumero = findViewById(R.id.etNumero)
        btnAdivinar = findViewById(R.id.btnAdivinar)
        tvHistorial = findViewById(R.id.tvHistorial)
        scrollView = findViewById(R.id.scrollView)

        if (numeroSecreto == 0){
            numeroSecreto = Random.nextInt(1, 101)
            tvHistorial.append("\n")
        }
        mostrarDialog()
        nuevoJuego()

        btnAdivinar.setOnClickListener {
            procesarIntento()
        }
    }

    private fun mostrarDialog() {

        val inputNombre = EditText(this)
        inputNombre.hint = "Escribe tu nombre"
        inputNombre.inputType = InputType.TYPE_CLASS_TEXT

        val dialog = AlertDialog.Builder(this)
            .setTitle("Nombre del jugador")
            .setMessage("Introduce tu nombre para comenzar")
            .setView(inputNombre)
            .setCancelable(false)
            .setPositiveButton("Aceptar", null)
            .create()

        dialog.setOnShowListener {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                val nombre = inputNombre.text.toString().trim()

                if (nombre.isEmpty()) {
                    inputNombre.error = "Introduce un nombre"
                } else {
                    nombreJugador = nombre
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
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

            clasificacion.add(
                Record(
                    nombreJugador,
                    intentos
                )
            )

            mostrarRanking()
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

    private fun mostrarRanking() {

        clasificacion.sortBy { it.intentos }

        var ranking = ""

        ranking += String.format(
            "%-5s %-15s %-10s\n",
            "POS",
            "NOMBRE",
            "INTENTOS"
        )

        ranking += "--------------------------------\n"

        clasificacion.forEachIndexed { index, jugador ->

            ranking += String.format("%-5d %-15s %-10d\n", index + 1, jugador.nombre, jugador.intentos
            )
        }

        val tvRanking = TextView(this)
        tvRanking.typeface = android.graphics.Typeface.MONOSPACE
        tvRanking.text = ranking

        AlertDialog.Builder(this)
            .setTitle("Ranking")
            .setView(tvRanking)
            .setPositiveButton("Nueva partida") { _, _ ->
                nuevoJuego()
                mostrarDialog()
            }
            .show()
    }


    companion object {
        var numeroSecreto = 0
        var intentos = 0
    }
}