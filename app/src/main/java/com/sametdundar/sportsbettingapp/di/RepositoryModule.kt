package com.sametdundar.sportsbettingapp.di

import com.sametdundar.sportsbettingapp.data.remote.OddsApiService
import com.sametdundar.sportsbettingapp.data.repository.OddsRepositoryImpl
import com.sametdundar.sportsbettingapp.domain.repository.OddsRepository
import com.sametdundar.sportsbettingapp.domain.usecase.GetOddsUseCase
import com.sametdundar.sportsbettingapp.domain.usecase.GetSportsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.SharedPreferences
import android.content.Context
import androidx.room.Room
import com.sametdundar.sportsbettingapp.data.local.AppDatabase
import com.sametdundar.sportsbettingapp.data.local.CouponDao
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideOddsRepository(
        api: OddsApiService,
        sharedPreferences: SharedPreferences
    ): OddsRepository = OddsRepositoryImpl(api, sharedPreferences)

    @Provides
    @Singleton
    fun provideGetOddsUseCase(repository: OddsRepository): GetOddsUseCase = GetOddsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSportsUseCase(repository: OddsRepository): GetSportsUseCase = GetSportsUseCase(repository)

    @Provides
    @Singleton
    fun provideBasketManager(): BasketManager = BasketManager()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sports_betting_db"
        ).build()

    @Provides
    @Singleton
    fun provideCouponDao(db: AppDatabase): CouponDao = db.couponDao()
} 