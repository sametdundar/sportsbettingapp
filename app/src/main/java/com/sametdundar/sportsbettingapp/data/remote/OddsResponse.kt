package com.sametdundar.sportsbettingapp.data.remote

data class OddsResponse(
    val id: String,
    val sport_key: String,
    val commence_time: String,
    val home_team: String,
    val away_team: String,
    val bookmakers: List<BookmakerResponse>
)

data class BookmakerResponse(
    val key: String,
    val title: String,
    val last_update: String?,
    val markets: List<MarketResponse>
)

data class MarketResponse(
    val key: String,
    val outcomes: List<OutcomeResponse>
)

data class OutcomeResponse(
    val name: String,
    val price: Double?,
    val point: Double? = null,
    val description: String? = null,
    val sid: String? = null
) 