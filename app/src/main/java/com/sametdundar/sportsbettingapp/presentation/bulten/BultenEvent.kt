package com.sametdundar.sportsbettingapp.presentation.bulten

sealed class BultenEvent {
    object Refresh : BultenEvent()
    // Diğer eventler buraya eklenebilir
} 