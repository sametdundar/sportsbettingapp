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
    // Diğer eventler buraya eklenebilir
} 