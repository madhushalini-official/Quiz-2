package com.Madhu.moodtracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Madhu.moodtracker.data.MoodEntry
import com.Madhu.moodtracker.viewmodel.MoodViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    moodId: Long,
    viewModel: MoodViewModel,
    onNavigateBack: () -> Unit
) {
    var moodEntry by remember { mutableStateOf<MoodEntry?>(null) }
    var noteText by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(moodId) {
        scope.launch {
            val entry = viewModel.getMoodById(moodId)
            moodEntry = entry
            entry?.let {
                noteText = it.note
                selectedMood = it.mood
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mood Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            moodEntry?.let { entry ->
                                val updatedEntry = entry.copy(
                                    mood = selectedMood,
                                    note = noteText
                                )
                                viewModel.updateMoodEntry(updatedEntry)
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        moodEntry?.let { entry ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Current Mood Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = selectedMood.split(" ").getOrNull(1) ?: "😊",
                            fontSize = 72.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = selectedMood,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = entry.getFormattedTimestamp(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Change Mood Section
                Text(
                    text = "Change Mood",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    viewModel.availableMoods.forEach { mood ->
                        val (moodName, emoji) = mood.split(" ")
                        val isSelected = mood == selectedMood

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .aspectRatio(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            ),
                            onClick = { selectedMood = mood }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = emoji,
                                    fontSize = 28.sp
                                )
                                Text(
                                    text = moodName,
                                    fontSize = 9.sp,
                                    color = if (isSelected)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Journal Note Section
                Text(
                    text = "Add a Note",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = { Text("Write your thoughts here...") },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "💡 Tip: Journaling your feelings can help you understand your mood patterns better.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}