package com.example.dividamos.apiservice

import com.example.dividamos.Gasto
import com.example.dividamos.Grupo
import com.example.dividamos.Usuario
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.35:8080/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
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

    @POST("usuarios/login")  // Path variables in URL
    fun log(
        @Body usuario: Usuario
    ): Call<Usuario>

    @POST("usuarios/crear")  // Path variables in URL
    fun create(
        @Body usuario: Usuario
    ): Call<Usuario>

    @GET("grupos/get/{id}")  // Path variables in URL
    fun getGrupos(
        @Path("id") id: Int,
    ): Call<List<Grupo>>
    @GET("grupos/getGastos/{id}/{idGrupo}")  // Path variables in URL
    fun getGastos(
        @Path("id") id: Int,
        @Path("idGrupo") idGrupo: Int,
    ): Call<List<Gasto>>
}

