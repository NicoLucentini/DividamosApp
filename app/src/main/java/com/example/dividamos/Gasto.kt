package com.example.dividamos

data class Gasto(
    val detalle: String,
    val nombrePagador: String,
    val nombrePrestados: List<String>,
    val monto: Float
)
