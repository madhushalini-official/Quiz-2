package com.Madhu.moodtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.Madhu.moodtracker.data.MoodDatabase
import com.Madhu.moodtracker.data.MoodEntry
import com.Madhu.moodtracker.data.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class MoodViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MoodRepository

    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries.asStateFlow()

    private val _weeklyMoodData = MutableStateFlow<Map<String, Int>>(emptyMap())
    val weeklyMoodData: StateFlow<Map<String, Int>> = _weeklyMoodData.asStateFlow()

    val availableMoods = listOf(
        "Happy 😊",
        "Calm 🙂",
        "Neutral 😐",
        "Sad 😟",
        "Anxious 😬"
    )

    init {
        val moodDao = MoodDatabase.getDatabase(application).moodDao()
        repository = MoodRepository(moodDao)

        viewModelScope.launch {
            repository.allMoods.collect { moods ->
                _moodEntries.value = moods
            }
        }
    }

    fun addMoodEntry(mood: String) {
        viewModelScope.launch {
            val newEntry = MoodEntry(mood = mood)
            repository.insertMood(newEntry)
        }
    }

    fun deleteMoodEntry(mood: MoodEntry) {
        viewModelScope.launch {
            repository.deleteMood(mood)
        }
    }

    fun updateMoodEntry(mood: MoodEntry) {
        viewModelScope.launch {
            repository.updateMood(mood)
        }
    }

    suspend fun getMoodById(id: Long): MoodEntry? {
        return repository.getMoodById(id)
    }

    fun loadWeeklyData() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            val sevenDaysAgo = calendar.timeInMillis

            repository.getMoodsFromDate(sevenDaysAgo).collect { moods ->
                val moodCounts = moods.groupingBy {
                    it.mood.split(" ")[0] // Get just the mood name without emoji
                }.eachCount()
                _weeklyMoodData.value = moodCounts
            }
        }
    }
}