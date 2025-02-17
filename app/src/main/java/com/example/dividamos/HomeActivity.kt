package com.example.dividamos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Get the name passed from LoginActivity
        val nombre = intent.getStringExtra("nombre")

        // Find the TextView to display the welcome message
        val welcomeTextView = findViewById<TextView>(R.id.textViewWelcome)

        // Set the text to "Welcome, [userName]"
        if (nombre != null) {
            welcomeTextView.text = "Welcome, $nombre!"
        }
    }
}