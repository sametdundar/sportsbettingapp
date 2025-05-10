package com.sametdundar.sportsbettingapp.presentation.mac

import com.sametdundar.sportsbettingapp.domain.model.SelectedBet

data class MacState(
    val selectedBets: List<SelectedBet> = emptyList(),
    val kuponBedeli: String = "50",
    val toplamOran: Double = 0.0,
    val maksKazanc: Double = 0.0,
    val showDeleteDialog: Boolean = false,
    val showSaveCouponDialog: Boolean = false
) 