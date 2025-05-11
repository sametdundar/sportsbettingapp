package com.sametdundar.sportsbettingapp.domain.repository

import com.sametdundar.sportsbettingapp.domain.model.SelectedBet
import kotlinx.coroutines.flow.StateFlow

interface BasketRepository {
    val selectedBets: StateFlow<List<SelectedBet>>
    fun addBet(bet: SelectedBet)
    fun removeBet(bet: SelectedBet)
    fun clearBets()
} 