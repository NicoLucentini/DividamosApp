package com.example.dividamos.apiservice

import android.widget.Button
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
fun <T> callRetrofitWithButton(
    call: Call<T>,
    onSuccess: (T?) -> Unit,
    onError: (String) -> Unit,
    onFailure: (Throwable) -> Unit,
    button : Button
){
    button.isEnabled = false
    callRetrofit(
        call = call,
        onSuccess = {b -> button.isEnabled = true; onSuccess(b)},
        onError = {e -> button.isEnabled = true; onError(e)},
        onFailure = {t -> button.isEnabled = true; onFailure(t)}
    )
}
