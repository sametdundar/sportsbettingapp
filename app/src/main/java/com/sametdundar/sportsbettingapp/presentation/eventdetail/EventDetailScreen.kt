package com.sametdundar.sportsbettingapp.presentation.eventdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun EventDetailScreen(
    sportKey: String,
    eventId: String,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(sportKey, eventId) {
        viewModel.onEvent(EventDetailEvent.LoadOdds(sportKey, eventId))
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error ?: "Bilinmeyen hata", color = MaterialTheme.colorScheme.error)
                }
            }
            state.odds != null -> {
                Text(text = "${state.odds!!.homeTeam} vs ${state.odds!!.awayTeam}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                state.odds!!.bookmakers.forEach { bookmaker ->
                    Text(text = bookmaker.title, style = MaterialTheme.typography.titleMedium)
                    bookmaker.markets.forEach { market ->
                        Text(text = "Market: ${market.key}", style = MaterialTheme.typography.bodyMedium)
                        market.outcomes.forEach { outcome ->
                            Text(text = "${outcome.name}: ${outcome.price}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
} 