package com.sametdundar.sportsbettingapp.domain.repository

import com.sametdundar.sportsbettingapp.domain.model.Odds
import com.sametdundar.sportsbettingapp.domain.model.Sport

interface OddsRepository {
    suspend fun getSports(): List<Sport>
    suspend fun getOdds(sportKey: String, regions: String, markets: String): List<Odds>
} 