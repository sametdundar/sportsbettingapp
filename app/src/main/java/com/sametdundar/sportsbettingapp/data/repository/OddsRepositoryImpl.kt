package com.sametdundar.sportsbettingapp.data.repository

import com.sametdundar.sportsbettingapp.data.mapper.toDomain
import com.sametdundar.sportsbettingapp.data.remote.OddsApiService
import com.sametdundar.sportsbettingapp.domain.model.Odds
import com.sametdundar.sportsbettingapp.domain.model.Sport
import com.sametdundar.sportsbettingapp.domain.repository.OddsRepository

class OddsRepositoryImpl(
    private val api: OddsApiService,
    private val apiKey: String
) : OddsRepository {
    override suspend fun getSports(): List<Sport> {
        return api.getSports(apiKey).map { it.toDomain() }
    }

    override suspend fun getOdds(sportKey: String, regions: String, markets: String): List<Odds> {
        return api.getOdds(sportKey, apiKey, regions, markets).map { it.toDomain() }
    }
} 