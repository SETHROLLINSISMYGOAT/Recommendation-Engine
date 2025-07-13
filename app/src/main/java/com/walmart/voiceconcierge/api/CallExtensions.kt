package com.walmart.voiceconcierge.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.enqueue(
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit
) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful && response.body() != null) {
                onSuccess(response.body()!!)
            } else onError("Err code: ${response.code()}")
        }
        override fun onFailure(call: Call<T>, t: Throwable) = onError("Fail: ${t.message}")
    })
}
