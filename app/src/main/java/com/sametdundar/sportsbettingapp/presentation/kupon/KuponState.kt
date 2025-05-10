package com.sametdundar.sportsbettingapp.presentation.kupon

import com.sametdundar.sportsbettingapp.domain.model.Coupon

data class KuponState(
    val coupons: List<Coupon> = emptyList()
) 