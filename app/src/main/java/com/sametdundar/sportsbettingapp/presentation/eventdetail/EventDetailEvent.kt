package com.sametdundar.sportsbettingapp.presentation.eventdetail

sealed class EventDetailEvent {
    data class LoadOdds(val sportKey: String, val eventId: String) : EventDetailEvent()
} 