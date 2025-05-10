package com.sametdundar.sportsbettingapp.domain.usecase

import com.sametdundar.sportsbettingapp.domain.model.Odds
import com.sametdundar.sportsbettingapp.domain.repository.OddsRepository
import javax.inject.Inject

class GetEventOddsUseCase @Inject constructor(
    private val repository: OddsRepository
) {
    suspend operator fun invoke(
        sportKey: String,
        eventId: String,
        regions: String = "us",
        markets: String = "h2h,spreads,totals"
    ): Odds {
        return repository.getEventOdds(sportKey, eventId, regions, markets)
    }
} 