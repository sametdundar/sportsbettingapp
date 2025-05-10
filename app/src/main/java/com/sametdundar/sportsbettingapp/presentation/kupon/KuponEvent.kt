package com.sametdundar.sportsbettingapp.presentation.kupon

sealed class KuponEvent {
    object LoadCoupons : KuponEvent()
    // Gerekirse başka eventler de eklenebilir
} 