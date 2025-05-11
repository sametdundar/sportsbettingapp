package com.sametdundar.sportsbettingapp.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import android.os.Bundle

@Singleton
class FirebaseAnalyticsService @Inject constructor(@ApplicationContext context: Context) : AnalyticsService {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun logEvent(eventName: String, params: Map<String, Any?>) {
        val bundle = Bundle()
        params.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Double -> bundle.putDouble(key, value)
                is Float -> bundle.putFloat(key, value)
                is Long -> bundle.putLong(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                else -> if (value != null) bundle.putString(key, value.toString())
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }
} 