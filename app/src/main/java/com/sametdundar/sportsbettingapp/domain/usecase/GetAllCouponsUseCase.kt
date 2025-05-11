package com.sametdundar.sportsbettingapp.domain.usecase

import com.sametdundar.sportsbettingapp.data.local.CouponDao
import com.sametdundar.sportsbettingapp.domain.model.Coupon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCouponsUseCase @Inject constructor(
    private val couponDao: CouponDao
) {
    operator fun invoke(): Flow<List<Coupon>> = couponDao.getAllCoupons()
} 