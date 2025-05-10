package com.sametdundar.sportsbettingapp.presentation.bulten

import com.sametdundar.sportsbettingapp.domain.model.Sport
import com.sametdundar.sportsbettingapp.domain.model.Odds

data class BultenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val sports: List<Sport> = emptyList(),
    val selectedGroup: String? = null,
    val groups: List<String> = emptyList(),
    val showApiKeyDialog: Boolean = false,
    val currentApiKey: String = "",
    val selectedSportIndex: Int = 0,
    val odds: List<Odds> = emptyList(),
    val searchQuery: String = "",
    val selectedOdds: Map<String, String> = emptyMap() // oddsId -> outcomeName
    // Buraya bülten verileri eklenecek
) 