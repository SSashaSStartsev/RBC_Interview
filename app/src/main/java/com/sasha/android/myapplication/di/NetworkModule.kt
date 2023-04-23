package com.sasha.android.myapplication.di

import com.sasha.android.myapplication.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideWeatherRepository(): WeatherRepository {
        return WeatherRepository
    }
}