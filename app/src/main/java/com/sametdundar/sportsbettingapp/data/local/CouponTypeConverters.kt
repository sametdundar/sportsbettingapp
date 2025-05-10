package com.sametdundar.sportsbettingapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sametdundar.sportsbettingapp.domain.model.SelectedBet

class CouponTypeConverters {
    @TypeConverter
    fun fromSelectedBetList(list: List<SelectedBet>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toSelectedBetList(json: String): List<SelectedBet> {
        val type = object : TypeToken<List<SelectedBet>>() {}.type
        return Gson().fromJson(json, type)
    }
} 