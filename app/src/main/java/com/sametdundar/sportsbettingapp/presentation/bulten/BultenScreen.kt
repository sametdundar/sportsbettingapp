package com.sametdundar.sportsbettingapp.presentation.bulten

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun BultenScreen(
    viewModel: BultenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var newApiKey by remember(state.showApiKeyDialog) { mutableStateOf(state.currentApiKey) }
    val scrollState = rememberScrollState()
    val sportScrollState = rememberScrollState()
    var selectedSport by remember(state.selectedGroup) { mutableStateOf(0) }

    if (state.showApiKeyDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(BultenEvent.HideApiKeyDialog) },
            title = { Text("API Key Değiştir") },
            text = {
                Column {
                    Text("Mevcut API Key:")
                    OutlinedTextField(
                        value = newApiKey,
                        onValueChange = { newApiKey = it },
                        label = { Text("Yeni API Key") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.onEvent(BultenEvent.ChangeApiKey(newApiKey))
                }) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                Button(onClick = { viewModel.onEvent(BultenEvent.HideApiKeyDialog) }) {
                    Text("İptal")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF388E3C))
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        if (state.groups.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 8.dp)
            ) {
                state.groups.forEach { group ->
                    val isSelected = state.selectedGroup == group
                    Text(
                        text = group,
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { viewModel.onEvent(BultenEvent.SelectGroup(group)) }
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        color = if (isSelected) Color(0xFF388E3C) else Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = state.error ?: "Bilinmeyen hata",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.onEvent(BultenEvent.ShowApiKeyDialog) }) {
                            Text("Change API key")
                        }
                    }
                }
            }
            else -> {
                val filteredSports = state.sports.filter { it.group == state.selectedGroup }
                if (filteredSports.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .horizontalScroll(sportScrollState)
                            .padding(horizontal = 8.dp)
                    ) {
                        filteredSports.forEachIndexed { index, sport ->
                            val isSelected = selectedSport == index
                            Text(
                                text = sport.title,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(if (isSelected) Color.White else Color.Transparent)
                                    .clickable { selectedSport = index }
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                color = if (isSelected) Color(0xFF388E3C) else Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredSports) { sport ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = sport.title, style = MaterialTheme.typography.titleMedium)
                                Text(text = sport.description ?: "", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
} 