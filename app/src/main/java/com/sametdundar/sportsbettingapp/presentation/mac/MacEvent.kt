package com.sametdundar.sportsbettingapp.presentation.mac

sealed class MacEvent {
    object LoadSelectedBets : MacEvent()
    data class KuponBedeliChanged(val value: String) : MacEvent()
    object ShowDeleteDialog : MacEvent()
    object HideDeleteDialog : MacEvent()
    object DeleteAllBets : MacEvent()
    object SaveCoupon : MacEvent()
    object ShowSaveCouponDialog : MacEvent()
    object HideSaveCouponDialog : MacEvent()
    // Gerekirse başka eventler de eklenebilir
} 