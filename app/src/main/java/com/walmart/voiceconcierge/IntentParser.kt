package com.walmart.voiceconcierge

object IntentParser {
    data class Intent(
        val category: String,
        val maxPrice: Int? = null,
        val minLikes: Int = 0,
        val state: String? = null
    )

    fun parse(input: String): Intent {
        val lower = input.lowercase()
        val maxPrice = Regex("under ?₹?(\\d+)").find(lower)?.groupValues?.get(1)?.toIntOrNull()
        val category = when {
            "chocolate" in lower -> "chocolate"
            "juice" in lower -> "juice"
            "snack" in lower -> "snacks"
            "dairy" in lower -> "dairy"
            "ready" in lower || "noodle" in lower -> "ready to eat"
            "rice" in lower -> "grains"
            else -> lower.split(" ").firstOrNull() ?: "snacks"
        }
        val likes = if ("popular" in lower || "most liked" in lower) 7 else 0
        val state = when {
            "bihar" in lower -> "Bihar"
            "punjab" in lower -> "Punjab"
            "delhi" in lower -> "Delhi"
            else -> null
        }

        return Intent(category, maxPrice, likes, state)
    }
}
