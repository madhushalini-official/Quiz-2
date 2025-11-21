package com.Madhu.moodtracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE id = :id")
    suspend fun getMoodById(id: Long): MoodEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntry)

    @Update
    suspend fun updateMood(mood: MoodEntry)

    @Delete
    suspend fun deleteMood(mood: MoodEntry)

    @Query("SELECT * FROM mood_entries WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    fun getMoodsFromDate(startTime: Long): Flow<List<MoodEntry>>
}