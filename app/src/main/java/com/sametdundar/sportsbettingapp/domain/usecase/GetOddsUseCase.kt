package com.sametdundar.sportsbettingapp.domain.usecase

import com.sametdundar.sportsbettingapp.domain.model.Odds
import com.sametdundar.sportsbettingapp.domain.repository.OddsRepository

class GetOddsUseCase(private val repository: OddsRepository) {
    suspend operator fun invoke(sportKey: String, regions: String, markets: String): List<Odds> =
        repository.getOdds(sportKey, regions, markets)
} 