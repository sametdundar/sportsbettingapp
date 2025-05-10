package com.sametdundar.sportsbettingapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OddsApiService {
    @GET("v4/sports/")
    suspend fun getSports(@Query("apiKey") apiKey: String): List<SportResponse>

    @GET("v4/sports/{sportKey}/odds/")
    suspend fun getOdds(
        @Path("sportKey") sportKey: String,
        @Query("apiKey") apiKey: String,
        @Query("regions") regions: String,
        @Query("markets") markets: String
    ): List<OddsResponse>

    @GET("v4/sports/{sportKey}/events/{eventId}/odds/")
    suspend fun getEventOdds(
        @Path("sportKey") sportKey: String,
        @Path("eventId") eventId: String,
        @Query("apiKey") apiKey: String,
        @Query("regions") regions: String = "us",
        @Query("markets") markets: String = "h2h"
    ): OddsResponse
} 