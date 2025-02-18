package com.example.dividamos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        //Handle register Button
        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val password = editTextPassword.text.toString()

            sendCrearRequest(name, password,this)
        }

        // Handle Login Button
        buttonLogin.setOnClickListener {
            val name = editTextName.text.toString()
            val password = editTextPassword.text.toString()

            sendLoginRequest(name, password, this)
        }
    }
    // Function to Send Login Request
    fun sendLoginRequest(nombre: String, password: String, context: android.content.Context) {
        apiService.login(nombre, password).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onLoginSuccesfull(nombre)
                } else {
                    Toast.makeText(context, "Login failed!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun sendCrearRequest(nombre: String, password: String, context: android.content.Context) {
        apiService.crear(nombre, password).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                  onLoginSuccesfull(nombre)
                } else {
                    Toast.makeText(context, "Login failed!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun onLoginSuccesfull(nombre: String){
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        intent.putExtra("nombre", nombre);
        startActivity(intent)
    }
}

// Retrofit API Interface
interface ApiService {
    @GET("usuarios/login/{nombre}/{password}")  // Path variables in URL
    fun login(
        @Path("nombre") nombre: String,
        @Path("password") password: String
    ): Call<Void>

    @POST("usuarios/crear/{nombre}/{password}")  // Path variables in URL
    fun crear(
        @Path("nombre") nombre: String,
        @Path("password") password: String
    ): Call<String>
}

// Retrofit Instance
val retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.1.35:8080/")
    .addConverterFactory(ScalarsConverterFactory.create())// Change to your actual API URL
    .addConverterFactory(GsonConverterFactory.create())  // For JSON responses
    .build()

val apiService = retrofit.create(ApiService::class.java)


