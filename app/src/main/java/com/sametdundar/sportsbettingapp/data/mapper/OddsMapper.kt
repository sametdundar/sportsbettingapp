package com.sametdundar.sportsbettingapp.data.mapper

import com.sametdundar.sportsbettingapp.data.remote.*
import com.sametdundar.sportsbettingapp.domain.model.*

fun OddsResponse.toDomain(): Odds = Odds(
    id = id,
    sportKey = sport_key,
    commenceTime = commence_time,
    homeTeam = home_team,
    awayTeam = away_team,
    bookmakers = bookmakers.map { it.toDomain() }
)

fun BookmakerResponse.toDomain(): Bookmaker = Bookmaker(
    key = key,
    title = title,
    lastUpdate = last_update,
    markets = markets.map { it.toDomain() }
)

fun MarketResponse.toDomain(): Market = Market(
    key = key,
    outcomes = outcomes.map { it.toDomain() }
)

fun OutcomeResponse.toDomain(): Outcome = Outcome(
    name = name,
    price = price,
    point = point,
    description = description,
    sid = sid
) 