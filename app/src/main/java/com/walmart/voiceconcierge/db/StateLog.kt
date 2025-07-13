package com.walmart.voiceconcierge.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StateLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val stateCode: String,
    val score: Int
)
