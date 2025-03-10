package com.example.dividamos

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dividamos.apiservice.RetrofitClient
import com.example.dividamos.apiservice.callRetrofit
import com.example.dividamos.entities.CalcularGastosResponse
import com.example.dividamos.entities.Gasto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GrupoActivity : AppCompatActivity() {
    private lateinit var welcomeText: TextView
    private lateinit var buttonContainer: LinearLayout

    var idGrupo = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupo)

        // Get references
        welcomeText = findViewById(R.id.textViewWelcome)
        buttonContainer = findViewById(R.id.buttonContainer)
        val addGastoButton = findViewById<Button>(R.id.btnAgregarGasto)
        val liquidarGastosButton = findViewById<Button>(R.id.btnLiquidarGastos)
        val addParticipante = findViewById<Button>(R.id.btnAgregarPartipante)
        val editTextAddParticipante = findViewById<EditText>(R.id.editTextParticipantEmail)
        // Retrieve username from Intent
        idGrupo = intent.getIntExtra("idGrupo", 0)
        setTitle("Grupo ${HomeActivity.grupo_data!!.nombre}!")
        welcomeText.text = "Estos son tus gastos"

        // Fetch data from API
        fetchGastos()

        addGastoButton.setOnClickListener{
            val dialog = GastoPopupFragment()
            dialog.onStart(idGrupo, this, null)
            dialog.show(supportFragmentManager, "GastoPopupFragment")
            dialog.onDestroy().apply { fetchGastos() }
        }
        liquidarGastosButton.setOnClickListener{
            sendLiquidarGastos(idGrupo)
        }
        addParticipante.setOnClickListener{
            sendAddParticipantRequest(idGrupo, editTextAddParticipante.text.toString(), this@GrupoActivity)
        }
    }

    private fun sendLiquidarGastos(idGrupo: Int) {
        RetrofitClient.apiService.liquidarGastos(idGrupo).enqueue(object : Callback<CalcularGastosResponse> {
            override fun onResponse(call: Call<CalcularGastosResponse>, response: Response<CalcularGastosResponse>) {
                if (response.isSuccessful) {
                    val r = response.body()
                    val transacciones : List<Pair<String, Double>> = r?.transacciones!!.map { it -> Pair(it.deudor + " debe a " + it. acreedor, it.monto.toDouble()) }.toList()
                    val gastosPorPersona : List<Pair<String, Double>> = r?.gastoPorPersonas!!.map { it -> Pair(it.nombre + ", Monto", it.gasto.toDouble()) }
                    showTransactionDialog(this@GrupoActivity,transacciones, gastosPorPersona)
                }
                else {
                    Log.e("API_ERROR", "Response unsuccessful")
                }
            }

            override fun onFailure(call: Call<CalcularGastosResponse>, t: Throwable) {
                Log.e("API_ERROR", "Response unsuccessful")
            }
        })
    }

    fun fetchGastos() {

        RetrofitClient.apiService.getGastos(HomeActivity.user_data?.id!!.toInt(),idGrupo).enqueue(object : Callback<List<Gasto>> {
            override fun onResponse(call: Call<List<Gasto>>, response: Response<List<Gasto>>) {
                if (response.isSuccessful) {
                    response.body()?.let { createButtons(it) }
                } else {
                    Log.e("API_ERROR", "Response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<Gasto>>, t: Throwable) {
                Log.e("API_ERROR", "Response unsuccessful")
            }
        })
    }

    private fun sendAddParticipantRequest(idGrupo: Int, participant: String, context: Context) {
        callRetrofit(
            RetrofitClient.apiService.agregarParticipante(idGrupo, participant),
            onSuccess = {Toast.makeText(context, "Participante Agregado!", Toast.LENGTH_LONG).show()},
            onError = {e -> Toast.makeText(context, "Usuario no encontrado!", Toast.LENGTH_LONG).show()},
            onFailure = {t -> Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()}

        )
    }
    private fun createButtons(gastos: List<Gasto>) {
        buttonContainer.removeAllViews() // Clear previous buttons

        for (gasto in gastos) {
            val button = Button(this).apply {
                text = "Detalle: "+ gasto.detalle + ", Monto: " + gasto.monto
                setPadding(20, 20, 20, 20)

                setOnClickListener {

                    val dialog = GastoPopupFragment()
                    dialog.onStart(idGrupo, this@GrupoActivity, gasto)
                    dialog.show(supportFragmentManager, "GastoPopupFragment")
                    dialog.onDestroy().apply { fetchGastos() }
                    Toast.makeText(this@GrupoActivity, "Clicked: ${gasto.detalle}", Toast.LENGTH_SHORT).show()

                }
            }
            buttonContainer.addView(button) // Add button to LinearLayout
        }
    }

    fun showTransactionDialog(context: Context, transactions: List<Pair<String, Double>>, items: List<Pair<String, Double>>) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_dual_scroll, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val containerTransactions = dialogView.findViewById<LinearLayout>(R.id.containerTransactions)
        val containerItems = dialogView.findViewById<LinearLayout>(R.id.containerItems)
        val btnClose = dialogView.findViewById<Button>(R.id.btnCloseDialog)

        // Populate Transactions List
        transactions.forEach { (text, amount) ->
            val textView = TextView(context)
            textView.text = "$text: $$amount"
            textView.textSize = 14f
            textView.setPadding(8, 8, 8, 8)
            textView.setTextColor(Color.BLACK)
            containerTransactions.addView(textView)
        }

        // Populate Items List
        items.forEach { (name, amount) ->
            val textView = TextView(context)
            textView.text = "$name: $$amount"
            textView.textSize = 14f
            textView.setPadding(8, 8, 8, 8)
            textView.setTextColor(Color.BLACK)
            containerItems.addView(textView)
        }

        // Close Button
        btnClose.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}