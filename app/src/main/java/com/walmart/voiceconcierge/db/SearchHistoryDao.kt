package com.walmart.voiceconcierge.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SearchHistoryDao {
    @Insert
    suspend fun insert(history: SearchHistory)

    @Query("SELECT * FROM SearchHistory ORDER BY id DESC LIMIT 20")
    suspend fun getRecent(): List<SearchHistory>
}
