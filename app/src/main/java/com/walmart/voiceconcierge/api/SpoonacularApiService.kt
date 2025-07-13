package com.walmart.voiceconcierge.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class SpoonacularProduct(val id: Int, val title: String, val aisle: String?, val pricePerServing: Double?, val image: String?)
data class ProductResponse(val results: List<SpoonacularProduct>)

interface SpoonacularApiService {
    @GET("food/products/search")
    fun searchProducts(@Query("query") query: String, @Query("number") number: Int = 5, @Query("apiKey") apiKey: String = "15cc79ed72c149c4acf0d95610eb4fbb"): Call<ProductResponse>
}
