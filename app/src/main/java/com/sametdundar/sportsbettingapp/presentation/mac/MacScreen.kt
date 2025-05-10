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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MacScreen(viewModel: MacViewModel = hiltViewModel()) {
    val selectedBets by viewModel.basketManager.selectedBets.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Seçili Maçlar ve Oranlar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        if (selectedBets.isEmpty()) {
            Text(text = "Henüz oran seçilmedi.", color = Color.Gray)
        } else {
            selectedBets.forEach { bet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF6FAFF))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "${bet.homeTeam} - ${bet.awayTeam}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Seçili Oran: ${bet.outcomeName} @ ${bet.odd}", color = Color(0xFF388E3C))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Maç Zamanı: ${bet.matchTime}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF223047))
                    }
                }
            }
        }
    }
} 