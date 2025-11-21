package com.Madhu.moodtracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Madhu.moodtracker.data.MoodEntry
import com.Madhu.moodtracker.viewmodel.MoodViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MoodViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToReport: () -> Unit
) {
    val moodEntries by viewModel.moodEntries.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mood Tracker",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToReport) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Weekly Report"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Mood Selector Section
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            MoodSelector(
                moods = viewModel.availableMoods,
                onMoodSelected = { mood ->
                    viewModel.addMoodEntry(mood)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mood History Section
            Text(
                text = "Your Mood History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (moodEntries.isEmpty()) {
                EmptyStateView()
            } else {
                MoodHistoryList(
                    moodEntries = moodEntries,
                    onMoodClick = onNavigateToDetail,
                    onDeleteClick = { mood ->
                        viewModel.deleteMoodEntry(mood)
                    }
                )
            }
        }
    }
}

@Composable
fun MoodSelector(
    moods: List<String>,
    onMoodSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        moods.forEach { mood ->
            val (moodName, emoji) = mood.split(" ")
            val color = when (moodName) {
                "Happy" -> MaterialTheme.colorScheme.primary
                "Calm" -> MaterialTheme.colorScheme.secondary
                "Neutral" -> MaterialTheme.colorScheme.tertiary
                "Sad" -> MaterialTheme.colorScheme.error
                "Anxious" -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(containerColor = color),
                onClick = { onMoodSelected(mood) }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = moodName,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "📝",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No moods logged yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MoodHistoryList(
    moodEntries: List<MoodEntry>,
    onMoodClick: (Long) -> Unit,
    onDeleteClick: (MoodEntry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(moodEntries, key = { it.id }) { entry ->
            MoodHistoryItem(
                moodEntry = entry,
                onClick = { onMoodClick(entry.id) },
                onDeleteClick = { onDeleteClick(entry) }
            )
        }
    }
}

@Composable
fun MoodHistoryItem(
    moodEntry: MoodEntry,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = moodEntry.mood,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = moodEntry.getFormattedTimestamp(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (moodEntry.note.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = moodEntry.note,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                }
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete mood",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}