@file:OptIn(ExperimentalMaterial3Api::class)

package com.sametdundar.sportsbettingapp.presentation.mac

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun MacScreen(
    viewModel: MacViewModel = hiltViewModel(),
    onAllBetsCleared: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.onEvent(MacEvent.LoadSelectedBets)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            if (state.selectedBets.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Kupon Bedeli:",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = state.kuponBedeli,
                                onValueChange = { viewModel.onEvent(MacEvent.KuponBedeliChanged(it)) },
                                singleLine = true,
                                modifier = Modifier.width(100.dp),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 15.sp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedTextColor = Color.White,
                                    containerColor = Color.Black,
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.White,
                                    cursorColor = Color.White
                                )
                            )
                            Text(
                                text = "₺",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Toplam Oran:",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = String.format("%.2f", state.toplamOran),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Maks. Kazanç:",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = String.format("%.2f ₺", state.maksKazanc),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { viewModel.onEvent(MacEvent.ShowSaveCouponDialog) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                                modifier = Modifier.weight(1f).padding(end = 4.dp)
                            ) {
                                Text(text = "Hemen Oyna", color = Color.White)
                            }
                            Button(
                                onClick = { viewModel.onEvent(MacEvent.ShowDeleteDialog) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                modifier = Modifier.weight(1f).padding(start = 4.dp)
                            ) {
                                Text(text = "Sil", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
        if (state.selectedBets.isEmpty()) {
            item {
                Text(text = "Henüz oran seçilmedi.", color = Color.Gray, modifier = Modifier.padding(6.dp), fontSize = 13.sp)
            }
        } else {
            items(state.selectedBets) { bet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp, horizontal = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(7.dp)) {
                        Text(
                            text = "${bet.homeTeam} - ${bet.awayTeam}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF223047),
                            maxLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = formatMatchTime(bet.matchTime),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val label = when (bet.outcomeName) {
                                bet.homeTeam -> "MS 1"
                                bet.awayTeam -> "MS 2"
                                "Draw", "X", "Beraberlik" -> "MS X"
                                else -> bet.outcomeName
                            }
                            Card(
                                modifier = Modifier,
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEB3B)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF223047)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "@ ${bet.odd}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF388E3C)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(MacEvent.HideDeleteDialog) },
            title = { Text("Tüm maçları silmek istiyor musunuz?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(MacEvent.DeleteAllBets)
                    onAllBetsCleared()
                }) {
                    Text("Evet")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(MacEvent.HideDeleteDialog) }) {
                    Text("Hayır")
                }
            }
        )
    }
    if (state.showSaveCouponDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(MacEvent.HideSaveCouponDialog) },
            title = { Text("Kuponu kaydetmek ve oynamak istiyor musunuz?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(MacEvent.SaveCoupon)
                    viewModel.onEvent(MacEvent.HideSaveCouponDialog)
                }) {
                    Text("Evet")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(MacEvent.HideSaveCouponDialog) }) {
                    Text("Hayır")
                }
            }
        )
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