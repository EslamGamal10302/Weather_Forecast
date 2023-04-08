package com.example.weatherforecast.network

import com.example.weatherforecast.Forecast
import kotlinx.coroutines.flow.Flow

interface RemoteSource {
    suspend fun getCurrentWeather(latitude:Double,longitude:Double,language:String,units:String):Flow<Forecast>
}