package com.sametdundar.sportsbettingapp.presentation.bulten

sealed class BultenEvent {
    object Refresh : BultenEvent()
    data class SelectGroup(val group: String) : BultenEvent()
    object LoadSports : BultenEvent()
    object ShowApiKeyDialog : BultenEvent()
    object HideApiKeyDialog : BultenEvent()
    data class ChangeApiKey(val newKey: String) : BultenEvent()
    data class SelectSport(val index: Int) : BultenEvent()
    data class LoadOdds(val sportKey: String) : BultenEvent()
    data class SearchQueryChanged(val query: String) : BultenEvent()
    data class SelectOdd(val oddsId: String, val outcomeName: String) : BultenEvent()
    object ClearAllSelectedOdds : BultenEvent()
}