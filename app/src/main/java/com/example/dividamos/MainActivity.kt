package com.example.dividamos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
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
    ): Call<Void>
}

// Retrofit Instance
val retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.1.35:8080/")  // Change to your actual API URL
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)

// Composable Function for Login UI
@Composable
fun LoginScreen() {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                sendCrearRequest(name, password, context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear")
        }
        Button(
            onClick = {
                sendLoginRequest(name, password, context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

    }
}

// Function to Send Login Request
fun sendLoginRequest(nombre: String, password: String, context: android.content.Context) {
    apiService.login(nombre, password).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
fun sendCrearRequest(nombre: String, password: String, context: android.content.Context) {
    apiService.crear(nombre, password).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Crear successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
