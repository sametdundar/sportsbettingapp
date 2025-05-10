package com.sametdundar.sportsbettingapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sametdundar.sportsbettingapp.domain.model.Coupon
import kotlinx.coroutines.flow.Flow

@Dao
interface CouponDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: Coupon)

    @Query("SELECT * FROM coupons ORDER BY tarih DESC")
    fun getAllCoupons(): Flow<List<Coupon>>

    @Query("DELETE FROM coupons")
    suspend fun deleteAllCoupons()
} 