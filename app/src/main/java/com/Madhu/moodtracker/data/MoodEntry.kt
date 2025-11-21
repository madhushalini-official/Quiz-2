package com.Madhu.moodtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mood: String,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
) {
    fun getFormattedTimestamp(): String {
        return SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(timestamp))
    }

    fun getFormattedDate(): String {
        return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}