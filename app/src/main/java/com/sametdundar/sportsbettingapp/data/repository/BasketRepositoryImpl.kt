package com.sametdundar.sportsbettingapp.data.repository

import com.sametdundar.sportsbettingapp.di.AnalyticsService
import com.sametdundar.sportsbettingapp.domain.model.SelectedBet
import com.sametdundar.sportsbettingapp.domain.repository.BasketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BasketRepositoryImpl @Inject constructor(
    private val analyticsService: AnalyticsService
) : BasketRepository {
    private val _selectedBets = MutableStateFlow<List<SelectedBet>>(emptyList())
    override val selectedBets: StateFlow<List<SelectedBet>> = _selectedBets

    override fun addBet(bet: SelectedBet) {
        val current = _selectedBets.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.matchId == bet.matchId && it.marketKey == bet.marketKey }
        if (existingIndex != -1) {
            current[existingIndex] = bet
        } else {
            current.add(bet)
        }
        _selectedBets.value = current
        analyticsService.logEvent("add_to_cart", mapOf(
            "bet_id" to bet.sid,
            "match_id" to bet.matchId,
            "market_key" to bet.marketKey,
            "outcome_name" to bet.outcomeName
        ))
    }

    override fun removeBet(bet: SelectedBet) {
        val current = _selectedBets.value.toMutableList()
        current.removeAll { it.matchId == bet.matchId && it.marketKey == bet.marketKey }
        _selectedBets.value = current
        analyticsService.logEvent("remove_from_cart", mapOf(
            "bet_id" to bet.sid,
            "match_id" to bet.matchId,
            "market_key" to bet.marketKey,
            "outcome_name" to bet.outcomeName
        ))
    }

    override fun clearBets() {
        _selectedBets.value = emptyList()
    }
} 