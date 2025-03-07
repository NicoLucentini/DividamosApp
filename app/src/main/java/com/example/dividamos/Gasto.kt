package com.example.dividamos

data class Gasto(
    val detalle: String,
    val nombrePagador: String,
    val nombresPrestados: List<String>,
    val monto: Float,
    val id:Int
)
