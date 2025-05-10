package com.sametdundar.sportsbettingapp.domain.model

data class Odds(
    val id: String,
    val sportKey: String,
    val commenceTime: String,
    val homeTeam: String,
    val awayTeam: String,
    val bookmakers: List<Bookmaker>
)

data class Bookmaker(
    val key: String,
    val title: String,
    val lastUpdate: String?,
    val markets: List<Market>
)

data class Market(
    val key: String,
    val outcomes: List<Outcome>
)

data class Outcome(
    val name: String,
    val price: Double?,
    val point: Double? = null,
    val description: String? = null
) 