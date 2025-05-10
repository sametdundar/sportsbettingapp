package com.sametdundar.sportsbettingapp.di

import com.sametdundar.sportsbettingapp.domain.model.SelectedBet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BasketManager {
    private val _selectedBets = MutableStateFlow<List<SelectedBet>>(emptyList())
    val selectedBets: StateFlow<List<SelectedBet>> = _selectedBets

    fun addBet(bet: SelectedBet) {
        val current = _selectedBets.value.toMutableList()
        // Aynı maç ve marketten bir seçim varsa güncelle
        val existingIndex = current.indexOfFirst { it.matchId == bet.matchId && it.marketKey == bet.marketKey }
        if (existingIndex != -1) {
            current[existingIndex] = bet
        } else {
            current.add(bet)
        }
        _selectedBets.value = current
    }

    fun removeBet(bet: SelectedBet) {
        val current = _selectedBets.value.toMutableList()
        current.removeAll { it.matchId == bet.matchId && it.marketKey == bet.marketKey }
        _selectedBets.value = current
    }

    fun clearBets() {
        _selectedBets.value = emptyList()
    }
} 