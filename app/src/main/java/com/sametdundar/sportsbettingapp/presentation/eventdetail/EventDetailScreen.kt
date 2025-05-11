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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import com.sametdundar.sportsbettingapp.domain.model.SelectedBet
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import com.sametdundar.sportsbettingapp.MainViewModel
import com.sametdundar.sportsbettingapp.di.AnalyticsService
import androidx.compose.ui.platform.LocalContext
import javax.inject.Inject

@Composable
fun EventDetailScreen(
    sportKey: String,
    eventId: String,
    viewModel: EventDetailViewModel = hiltViewModel(),
    analyticsService: AnalyticsService = androidx.hilt.navigation.compose.hiltViewModel<MainViewModel>().let { hiltViewModel ->
        val context = LocalContext.current
        androidx.hilt.navigation.compose.hiltViewModel<MainViewModel>().let { hiltViewModel }
        androidx.hilt.navigation.compose.hiltViewModel<MainViewModel>().basketManager.let { basketManager ->
            val field = basketManager.javaClass.getDeclaredField("analyticsService")
            field.isAccessible = true
            field.get(basketManager) as AnalyticsService
        }
    }
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val basketManager = hiltViewModel<MainViewModel>().basketManager
    val selectedBets by basketManager.selectedBets.collectAsState()

    LaunchedEffect(sportKey, eventId, state.odds) {
        if (state.odds != null) {
            val odds = state.odds!!
            analyticsService.logEvent("match_detail", mapOf(
                "match_id" to odds.id,
                "home_team" to odds.homeTeam,
                "away_team" to odds.awayTeam
            ))
        }
    }

    LaunchedEffect(sportKey, eventId) {
        viewModel.onEvent(EventDetailEvent.LoadOdds(sportKey, eventId))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FAFF))
            .padding(12.dp)
            .verticalScroll(scrollState)
    ) {
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
                val odds = state.odds!!
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${odds.homeTeam} - ${odds.awayTeam}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        color = Color(0xFF223047)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatMatchTime(odds.commenceTime),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = Color(0xFF388E3C)
                    )
                }
                val bookmaker = odds.bookmakers.firstOrNull()
                if (bookmaker != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = bookmaker.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF223047)
                            )
                            bookmaker.markets.forEach { market ->
                                val marketExplanation = when (market.key) {
                                    "h2h" -> "Kazanan"
                                    "spreads" -> "Handikap"
                                    "totals" -> "Toplam"
                                    else -> market.key
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF223047), RoundedCornerShape(8.dp))
                                        .padding(vertical = 6.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = marketExplanation,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "(${market.key})",
                                        color = Color.White.copy(alpha = 0.7f),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    market.outcomes.forEach { outcome ->
                                        val outcomeId = outcome.sid ?: "${outcome.name}_${market.key}_${odds.id}"
                                        val isSelected = selectedBets.any {
                                            (it.sid ?: "${it.outcomeName}_${it.marketKey}_${it.matchId}") == outcomeId &&
                                            it.marketKey == market.key &&
                                            it.matchId == odds.id &&
                                            it.homeTeam == odds.homeTeam &&
                                            it.awayTeam == odds.awayTeam &&
                                            it.odd == outcome.price
                                        }
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isSelected) Color(0xFFFFEB3B) else Color(0xFFF6FAFF),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
                                                    selectedBets.filter {
                                                        it.marketKey == market.key &&
                                                        it.matchId == odds.id &&
                                                        it.homeTeam == odds.homeTeam &&
                                                        it.awayTeam == odds.awayTeam &&
                                                        it.odd == outcome.price
                                                    }.forEach { basketManager.removeBet(it) }
                                                    val bet = SelectedBet(
                                                        sid = outcomeId,
                                                        matchId = odds.id,
                                                        homeTeam = odds.homeTeam,
                                                        awayTeam = odds.awayTeam,
                                                        marketKey = market.key,
                                                        outcomeName = outcome.name,
                                                        odd = outcome.price,
                                                        matchTime = odds.commenceTime
                                                    )
                                                    if (!isSelected) {
                                                        basketManager.addBet(bet)
                                                    }
                                                }
                                                .padding(vertical = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = outcome.price.toString(),
                                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                                color = Color(0xFF388E3C)
                                            )
                                            Text(
                                                text = outcome.name,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFF223047)
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