package com.walmart.voiceconcierge

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Product(
    val id: Int,
    val title: String,
    val aisle: String?,
    val price: Double,
    val image: String,
    val likes: Int = 0,
    val cartAdds: Int = 0,
    val regionSales: Map<String, Int> = emptyMap()
)

object ProductRepository {

    private val mockProducts = listOf(
        Product(1, "Dark Chocolate Bar", "Snacks", 120.0, "https://via.placeholder.com/150", 8, 3, mapOf("Bihar" to 5)),
        Product(2, "Milk Chocolate", "Snacks", 90.0, "https://via.placeholder.com/150", 12, 7, mapOf("Delhi" to 10)),
        Product(3, "Greek Yogurt", "Dairy", 110.0, "https://via.placeholder.com/150", 9, 6, mapOf("Punjab" to 7)),
        Product(4, "Paneer Cubes", "Dairy", 140.0, "https://via.placeholder.com/150", 5, 9, mapOf("Punjab" to 10)),
        Product(5, "Protein Almonds", "Snacks", 160.0, "https://via.placeholder.com/150", 10, 6, mapOf("Gujarat" to 3)),
        Product(6, "Instant Noodles", "Ready to Eat", 45.0, "https://via.placeholder.com/150", 14, 9, mapOf("Uttar Pradesh" to 12)),
        Product(7, "Apple Juice", "Beverages", 85.0, "https://via.placeholder.com/150", 7, 2, mapOf("Bihar" to 3)),
        Product(8, "Almond Milk", "Dairy", 150.0, "https://via.placeholder.com/150", 13, 5, mapOf("Karnataka" to 6))
    )

    suspend fun recommend(
        intent: IntentParser.Intent,
        feedback: Map<String, Int>,
        statePrefs: Map<String, Int>
    ): List<Product> = withContext(Dispatchers.Default) {
        val filtered = mockProducts.filter {
            (it.title.contains(intent.category, ignoreCase = true)
                    || it.aisle?.contains(intent.category, ignoreCase = true) == true) &&
                    (intent.maxPrice?.let { max -> it.price <= max } ?: true) &&
                    it.likes >= intent.minLikes &&
                    (intent.state?.let { st -> it.regionSales[st] ?: 0 > 0 } ?: true)
        }

        val ranked = if (filtered.isNotEmpty()) filtered else mockProducts

        return@withContext ranked.sortedByDescending {
            val key = it.id.toString()
            (feedback[key] ?: 0) + (statePrefs[key] ?: 0) + it.likes + it.cartAdds
        }
    }
}
