package com.sametdundar.sportsbettingapp.domain.model

data class SelectedBet(
    val sid: String? = null,
    val matchId: String,
    val homeTeam: String,
    val awayTeam: String,
    val marketKey: String,
    val outcomeName: String,
    val odd: Double?,
    val matchTime: String
) 