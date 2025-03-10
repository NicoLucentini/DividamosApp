package com.example.dividamos.apiservice

import com.example.dividamos.entities.CalcularGastosResponse
import com.example.dividamos.entities.Gasto
import com.example.dividamos.entities.Grupo
import com.example.dividamos.entities.Usuario
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

object RetrofitClient {
    //private const val BASE_URL = "http://192.168.1.35:8080/"
    private const val BASE_URL = " https://dividamos.onrender.com/"

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

    @POST("grupos/crearGrupo")  // Path variables in URL
    fun crearGrupo(
        @Body grupo: Grupo
    ): Call<Void>
    @POST("grupos/agregarGasto/{idGrupo}")  // Path variables in URL
    fun crearGasto(
        @Path("idGrupo") idGrupo: Int,
        @Body gasto: Gasto
    ): Call<Void>
    @GET("grupos/calcularGastos/{idGrupo}")  // Path variables in URL
    fun liquidarGastos(
        @Path("idGrupo") idGrupo: Int
    ): Call<CalcularGastosResponse>
    @PUT("gastos/editarGasto/{id}")  // Path variables in URL
    fun editarGasto(
        @Path("id") id: Int,
        @Body gasto: Gasto
    ): Call<Void>
    @DELETE("gastos/eliminarGasto/{id}")  // Path variables in URL
    fun eliminarGasto(
        @Path("id") id: Int
    ): Call<Void>


    @GET("usuarios/findByEmail/{email}")  // Path variables in URL
    fun buscarPorEmail(
        @Path("email") email: String
    ): Call<Void>

    @POST("grupos/agregarParticipante/{idGrupo}/{participante}")  // Path variables in URL
    fun agregarParticipante(
        @Path("idGrupo") idGrupo: Int,
        @Path("participante") participante: String
    ): Call<Void>
}

