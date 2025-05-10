package com.sametdundar.sportsbettingapp.di

import com.sametdundar.sportsbettingapp.data.remote.OddsApiService
import com.sametdundar.sportsbettingapp.data.repository.OddsRepositoryImpl
import com.sametdundar.sportsbettingapp.domain.repository.OddsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.SharedPreferences

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideOddsRepository(
        api: OddsApiService,
        sharedPreferences: SharedPreferences
    ): OddsRepository = OddsRepositoryImpl(api, sharedPreferences)
} 