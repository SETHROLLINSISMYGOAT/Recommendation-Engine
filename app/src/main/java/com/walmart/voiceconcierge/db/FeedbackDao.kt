package com.walmart.voiceconcierge.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FeedbackDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(feedback: Feedback)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feedbackList: List<Feedback>)


    @Query("SELECT * FROM Feedback")
    suspend fun getAllScores(): List<Feedback>
}
