package com.sametdundar.sportsbettingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sametdundar.sportsbettingapp.domain.model.Coupon

@Database(entities = [Coupon::class], version = 1, exportSchema = false)
@TypeConverters(CouponTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun couponDao(): CouponDao
} 