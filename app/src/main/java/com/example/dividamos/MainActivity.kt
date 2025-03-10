package com.example.dividamos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.dividamos.apiservice.RetrofitClient
import com.example.dividamos.entities.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextEmail= findViewById<EditText>(R.id.editTextEmail)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        //Handle register Button
        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val password = editTextPassword.text.toString()
            val email = editTextEmail.text.toString();
            val usuario = Usuario(name, password, email, -1L)
            sendCrearRequest(usuario,this)
        }

        // Handle Login Button
        buttonLogin.setOnClickListener {
            val name = editTextName.text.toString()
            val password = editTextPassword.text.toString()
            val email = editTextEmail.text.toString();
            val usuario = Usuario(name, password, email, -1L)
            sendLoginRequest(usuario, this)
        }
    }
    // Function to Send Login Request
    fun sendLoginRequest(nombre: String, password: String, context: android.content.Context) {
        RetrofitClient.apiService.login(nombre, password).enqueue(object : Callback<Void> {
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
    fun sendLoginRequest(usuario: Usuario, context: android.content.Context) {
        RetrofitClient.apiService.log(usuario).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    //onLoginSuccesfull(usuario.nombre)
                    onLoginSuccesfull(response.body()!!)
                } else {
                    Toast.makeText(context, "Login failed!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun sendCrearRequest(nombre: String, password: String, context: android.content.Context) {
        RetrofitClient.apiService.crear(nombre, password).enqueue(object : Callback<String> {
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
    fun sendCrearRequest(usuario: Usuario, context: android.content.Context) {
        RetrofitClient.apiService.create(usuario).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    //onLoginSuccesfull(usuario.nombre)
                    onLoginSuccesfull(response.body()!!)
                } else {
                    Toast.makeText(context, "Login failed!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun onLoginSuccesfull(nombre: String){
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        intent.putExtra("nombre", nombre);
        startActivity(intent)
    }
    fun onLoginSuccesfull(usuario: Usuario){
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        intent.putExtra("user_data", usuario)
        //intent.putExtra("nombre", nombre);
        startActivity(intent)
    }
}




