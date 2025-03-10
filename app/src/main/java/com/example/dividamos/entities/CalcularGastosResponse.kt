package com.example.dividamos.entities

data class CalcularGastosResponse(
    val transacciones : List<Transaccion>,
    val gastoPorPersonas: List<GastoPorPersona>

)