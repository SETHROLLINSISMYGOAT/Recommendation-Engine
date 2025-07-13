package com.walmart.voiceconcierge.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StateLogDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(stateLog: StateLog)


    @Insert
    suspend fun insert(stateLog: StateLog)

    @Query("SELECT productId, score FROM StateLog WHERE stateCode = :stateCode")
    suspend fun getScoresByState(stateCode: String): List<StateScore>
}
