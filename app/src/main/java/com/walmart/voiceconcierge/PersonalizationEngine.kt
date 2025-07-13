package com.walmart.voiceconcierge

import com.walmart.voiceconcierge.api.ApiClient
import com.walmart.voiceconcierge.api.ProductResponse
import com.walmart.voiceconcierge.api.SpoonacularProduct
import com.walmart.voiceconcierge.api.enqueue

object PersonalizationEngine {
    interface Callback {
        fun onSuccess(products: List<SpoonacularProduct>)
        fun onFailure(error: String)
    }

    fun fetchRecommendations(query: String, callback: Callback) {
        ApiClient.apiService.searchProducts(query).enqueue(
            onSuccess = { response -> callback.onSuccess(response.results) },
            onError = { error -> callback.onFailure(error) }
        )
    }
}
