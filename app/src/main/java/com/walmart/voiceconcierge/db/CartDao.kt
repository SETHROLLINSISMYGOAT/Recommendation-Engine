package com.walmart.voiceconcierge.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface CartDao {
    @Insert
    suspend fun insert(item: CartItem)

    @Query("SELECT * FROM CartItem")
    suspend fun getAll(): List<CartItem>

    @Delete
    suspend fun delete(item: CartItem)
}
