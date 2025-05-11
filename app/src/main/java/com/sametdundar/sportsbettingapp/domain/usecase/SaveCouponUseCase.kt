package com.sametdundar.sportsbettingapp.domain.usecase

import com.sametdundar.sportsbettingapp.data.local.CouponDao
import com.sametdundar.sportsbettingapp.domain.model.Coupon
import javax.inject.Inject

class SaveCouponUseCase @Inject constructor(
    private val couponDao: CouponDao
) {
    suspend operator fun invoke(coupon: Coupon) {
        couponDao.insertCoupon(coupon)
    }
} 