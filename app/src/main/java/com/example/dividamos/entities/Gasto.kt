package com.example.dividamos.entities

data class Gasto(
    val detalle: String,
    val nombrePagador: String,
    val nombresPrestados: List<String>,
    val monto: Float,
    val id:Int
)
