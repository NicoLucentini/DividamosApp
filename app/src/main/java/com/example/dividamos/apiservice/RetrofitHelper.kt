package com.example.dividamos.apiservice

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> callRetrofit(
    call: Call<T>,
    onSuccess: (T?) -> Unit,
    onError: (String) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                onSuccess(response.body())
            } else {
                onError("Error ${response.code()}: ${response.errorBody()?.string() ?: "Unknown error"}")
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailure(t)
        }
    })
}