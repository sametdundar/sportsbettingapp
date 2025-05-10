package com.sametdundar.sportsbettingapp.presentation.kupon

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun KuponScreen(viewModel: KuponViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Kayıtlı Kuponlar",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF223047),
                modifier = Modifier.padding(start = 6.dp, bottom = 8.dp, top = 2.dp)
            )
        }
        if (state.coupons.isEmpty()) {
            item {
                Text(
                    text = "Henüz kupon yok.",
                    color = Color.Gray,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 15.sp
                )
            }
        } else {
            items(state.coupons) { coupon ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Tarih: ${formatDate(coupon.tarih)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kupon Bedeli: ${coupon.kuponBedeli} ₺",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Toplam Oran: %.2f".format(coupon.toplamOran),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Maks. Kazanç: %.2f ₺".format(coupon.maksKazanc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF223047),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Maçlar:",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        coupon.bets.forEach { bet ->
                            Text(
                                text = "- ${bet.homeTeam} - ${bet.awayTeam} | ${bet.outcomeName} @ ${bet.odd}",
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

fun formatDate(timeMillis: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timeMillis))
} 