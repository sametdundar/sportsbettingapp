@file:OptIn(ExperimentalMaterial3Api::class)

package com.sametdundar.sportsbettingapp.presentation.bulten

import android.util.Log
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.SnackbarHost
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import androidx.compose.runtime.LaunchedEffect

@Composable
fun BultenScreen(
    onNavigateToEventDetail: (String, String) -> Unit,
    onSelectedOddsChanged: (Int) -> Unit = {},
    viewModel: BultenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var newApiKey by remember(state.showApiKeyDialog) { mutableStateOf(state.currentApiKey) }
    val scrollState = rememberScrollState()
    val sportScrollState = rememberScrollState()
    var selectedSport by remember { mutableStateOf(state.selectedSportIndex) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.selectedOdds) {
        onSelectedOddsChanged(state.selectedOdds.size)
    }

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
        if (state.groups.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
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
                                    .clickable {
                                        selectedSport = index
                                        viewModel.onEvent(BultenEvent.SelectSport(index))
                                    }
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                color = if (isSelected) Color(0xFF388E3C) else Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.onEvent(BultenEvent.SearchQueryChanged(it)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        placeholder = { Text("Ara...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color(0xFF388E3C),
                            unfocusedBorderColor = Color(0xFF388E3C)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                val filteredOdds = if (state.searchQuery.isBlank()) {
                    state.odds
                } else {
                    state.odds.filter { odds ->
                        odds.homeTeam.contains(state.searchQuery, ignoreCase = true) ||
                        odds.awayTeam.contains(state.searchQuery, ignoreCase = true)
                    }
                }
                if (filteredOdds.isNotEmpty()) {
                    val groupedOdds = filteredOdds.groupBy {
                        val sport = state.sports.find { s -> s.key == it.sportKey }
                        Pair(sport?.title ?: it.sportKey, formatMatchTime(it.commenceTime))
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        groupedOdds.forEach { (leagueAndTime, oddsList) ->
                            item {
                                LeagueTimeHeader(league = leagueAndTime.first, time = leagueAndTime.second)
                            }
                            items(oddsList) { odds ->
                                val bookmaker = odds.bookmakers.firstOrNull()
                                val market = bookmaker?.markets?.firstOrNull()
                                val outcomes = market?.outcomes?.take(3) ?: emptyList()
                                MatchItemModern(
                                    homeTeam = odds.homeTeam,
                                    awayTeam = odds.awayTeam,
                                    odds = outcomes.map { outcome ->
                                        val label = when (outcome.name) {
                                            odds.homeTeam -> "MS 1"
                                            odds.awayTeam -> "MS 2"
                                            "Draw", "X", "Beraberlik" -> "MS X"
                                            else -> outcome.name
                                        }
                                        outcome.price.toString() to label
                                    },
                                    selectedOutcomeName = state.selectedOdds[odds.id],
                                    outcomeNames = outcomes.map { it.name },
                                    onOddClick = { idx ->
                                        val outcomeName = outcomes.getOrNull(idx)?.name ?: return@MatchItemModern
                                        viewModel.onEvent(BultenEvent.SelectOdd(odds.id, outcomeName))
                                    },
                                    onTeamsClick = {
                                        onNavigateToEventDetail(odds.sportKey, odds.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeagueTimeHeader(league: String, time: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .shadow(1.dp, RoundedCornerShape(12.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF223047), Color(0xFF3A4A6A))
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 6.dp, horizontal = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = league,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = time,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun MatchItemModern(
    homeTeam: String,
    awayTeam: String,
    odds: List<Pair<String, String>>,
    selectedOutcomeName: String?,
    outcomeNames: List<String>,
    onOddClick: (index: Int) -> Unit,
    onTeamsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .shadow(1.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onTeamsClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = homeTeam,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF223047)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = " - ",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFB0B0B0)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = awayTeam,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF223047)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            odds.forEachIndexed { index, (odd, label) ->
                val isSelected = selectedOutcomeName == outcomeNames.getOrNull(index)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFFFFEB3B) else Color(0xFFF6FAFF))
                        .border(1.dp, Color(0xFF388E3C), RoundedCornerShape(8.dp))
                        .clickable { onOddClick(index) }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = odd,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF388E3C)
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF223047)
                    )
                }
            }
        }
    }
}

fun formatMatchTime(commenceTime: String?): String {
    return try {
        if (commenceTime.isNullOrBlank()) return ""
        val zonedDateTime = ZonedDateTime.parse(commenceTime)
        val istanbulZone = ZoneId.of("Europe/Istanbul")
        val localDateTime = zonedDateTime.withZoneSameInstant(istanbulZone)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        localDateTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
}