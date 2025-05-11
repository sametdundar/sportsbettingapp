package com.sametdundar.sportsbettingapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coupons")
data class Coupon(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kuponBedeli: Double,
    val toplamOran: Double,
    val maksKazanc: Double,
    val tarih: Long = System.currentTimeMillis(),
    val bets: List<SelectedBet>
) 