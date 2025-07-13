package com.walmart.voiceconcierge.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.spoonacular.com/"
    val apiService: SpoonacularApiService by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(SpoonacularApiService::class.java)
    }
}
