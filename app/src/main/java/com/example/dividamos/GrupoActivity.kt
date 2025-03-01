package com.example.dividamos

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dividamos.apiservice.RetrofitClient
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

        // Retrieve username from Intent
        idGrupo = intent.getIntExtra("idGrupo", 0)
        setTitle("Gastos!")
        welcomeText.text = "This are your gastos"

        // Fetch data from API
        fetchGastos()

        addGastoButton.setOnClickListener{
            val dialog = GastoPopupFragment()
            dialog.onStart(idGrupo, this)
            dialog.show(supportFragmentManager, "GastoPopupFragment")
            dialog.onDestroy().apply { fetchGastos() }
        }
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

    private fun createButtons(grupos: List<Gasto>) {
        buttonContainer.removeAllViews() // Clear previous buttons

        for (grupo in grupos) {
            val button = Button(this).apply {
                text = "Detalle: "+ grupo.detalle + ", Monto: " + grupo.monto
                setPadding(20, 20, 20, 20)
                setOnClickListener {
                    Toast.makeText(this@GrupoActivity, "Clicked: ${grupo.detalle}", Toast.LENGTH_SHORT).show()


                }
            }
            buttonContainer.addView(button) // Add button to LinearLayout
        }
    }
}