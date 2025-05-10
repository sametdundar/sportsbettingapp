package com.sametdundar.sportsbettingapp.presentation.eventdetail

import com.sametdundar.sportsbettingapp.domain.model.Odds

data class EventDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val odds: Odds? = null
) 