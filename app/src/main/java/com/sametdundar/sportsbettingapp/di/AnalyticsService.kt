package com.sametdundar.sportsbettingapp.di

interface AnalyticsService {
    fun logEvent(eventName: String, params: Map<String, Any?> = emptyMap())
} 