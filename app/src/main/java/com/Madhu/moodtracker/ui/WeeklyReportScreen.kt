package com.Madhu.moodtracker.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Madhu.moodtracker.viewmodel.MoodViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyReportScreen(
    viewModel: MoodViewModel,
    onNavigateBack: () -> Unit
) {
    val weeklyData by viewModel.weeklyMoodData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeeklyData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weekly Mood Report") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Last 7 Days",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "See how your mood has been this week",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (weeklyData.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📊",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No data for the past 7 days",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start tracking your moods to see insights here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                MoodBarChart(weeklyData = weeklyData)

                Spacer(modifier = Modifier.height(24.dp))

                MoodSummaryCards(weeklyData = weeklyData)
            }
        }
    }
}

@Composable
fun MoodBarChart(weeklyData: Map<String, Int>) {
    val maxCount = weeklyData.values.maxOrNull() ?: 1
    val moodColors = mapOf(
        "Happy" to Color(0xFF4CAF50),
        "Calm" to Color(0xFF2196F3),
        "Neutral" to Color(0xFF9E9E9E),
        "Sad" to Color(0xFFFF9800),
        "Anxious" to Color(0xFFF44336)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Mood Frequency",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display bars using Compose components instead of Canvas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                weeklyData.entries.forEachIndexed { index, (mood, count) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mood,
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .padding(start = 8.dp)
                        ) {
                            // Background bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                // Filled bar based on count
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(count.toFloat() / maxCount)
                                        .height(40.dp)
                                        .padding(end = 4.dp)
                                ) {
                                    Card(
                                        modifier = Modifier.fillMaxSize(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = moodColors[mood] ?: Color.Gray
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Text(
                                                text = count.toString(),
                                                modifier = Modifier.padding(end = 8.dp),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoodSummaryCards(weeklyData: Map<String, Int>) {
    val totalEntries = weeklyData.values.sum()
    val mostFrequentMood = weeklyData.maxByOrNull { it.value }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Summary",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Entries",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = totalEntries.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(text = "📝", style = MaterialTheme.typography.displaySmall)
            }
        }

        mostFrequentMood?.let { (mood, count) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Most Frequent Mood",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = mood,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$count times this week",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = when(mood) {
                            "Happy" -> "😊"
                            "Calm" -> "🙂"
                            "Neutral" -> "😐"
                            "Sad" -> "😟"
                            "Anxious" -> "😬"
                            else -> "📊"
                        },
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }
    }
}