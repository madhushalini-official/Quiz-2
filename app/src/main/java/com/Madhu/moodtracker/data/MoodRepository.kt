package com.Madhu.moodtracker.data

import kotlinx.coroutines.flow.Flow

class MoodRepository(private val moodDao: MoodDao) {
    val allMoods: Flow<List<MoodEntry>> = moodDao.getAllMoods()

    suspend fun getMoodById(id: Long): MoodEntry? {
        return moodDao.getMoodById(id)
    }

    suspend fun insertMood(mood: MoodEntry) {
        moodDao.insertMood(mood)
    }

    suspend fun updateMood(mood: MoodEntry) {
        moodDao.updateMood(mood)
    }

    suspend fun deleteMood(mood: MoodEntry) {
        moodDao.deleteMood(mood)
    }

    fun getMoodsFromDate(startTime: Long): Flow<List<MoodEntry>> {
        return moodDao.getMoodsFromDate(startTime)
    }
}