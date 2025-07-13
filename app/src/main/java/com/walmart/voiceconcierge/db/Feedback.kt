package com.walmart.voiceconcierge.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Feedback(
    @PrimaryKey val productId: String,
    val score: Int
)
