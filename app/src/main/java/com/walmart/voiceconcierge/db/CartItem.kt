
package com.walmart.voiceconcierge.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartItem(
    @PrimaryKey val productId: String,
    val productName: String,
    val aisle: String?,
    val price: Double,
    val quantity: Int = 1
)
