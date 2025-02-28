package com.example.dividamos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dividamos.apiservice.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    private lateinit var welcomeText: TextView
    private lateinit var buttonContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Get references
        welcomeText = findViewById(R.id.textViewWelcome)
        buttonContainer = findViewById(R.id.buttonContainer)


        // Retrieve username from Intent
        //val username = intent.getStringExtra("nombre") ?: "Guest"
        val usuario : Usuario? = intent.getParcelableExtra<Usuario>("user_data")

        //val username = intent.getStringExtra("nombre") ?: "Guest"
        setTitle("Welcome , ${usuario?.nombre} , id ${usuario?.id}!")
        welcomeText.text = "This are your groups"

        // Fetch data from API
        fetchGrupos()
    }

    private fun fetchGrupos() {

        RetrofitClient.apiService.getGrupos(1).enqueue(object : Callback<List<Grupo>> {
            override fun onResponse(call: Call<List<Grupo>>, response: Response<List<Grupo>>) {
                if (response.isSuccessful) {
                    response.body()?.let { createButtons(it) }
                } else {
                    Log.e("API_ERROR", "Response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<Grupo>>, t: Throwable) {
                Log.e("API_ERROR", "Request failed: ${t.message}")
            }
        })
    }

    private fun createButtons(grupos: List<Grupo>) {
        buttonContainer.removeAllViews() // Clear previous buttons

        for (grupo in grupos) {
            val button = Button(this).apply {
                text = grupo.nombre
                setPadding(20, 20, 20, 20)
                setOnClickListener {
                    Toast.makeText(this@HomeActivity, "Clicked: ${grupo.nombre}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomeActivity, GrupoActivity::class.java)
                    intent.putExtra("idGrupo", grupo.id);
                    startActivity(intent)

                }
            }
            buttonContainer.addView(button) // Add button to LinearLayout
        }
    }
}
