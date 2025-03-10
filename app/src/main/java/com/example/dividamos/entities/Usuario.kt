package com.example.dividamos.entities
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario  (
    val nombre:String,
    val password:String,
    val email:String,
    val id:Long
) : Parcelable