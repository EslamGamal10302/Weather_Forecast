package com.example.weatherforecast.network

import com.example.weatherforecast.Forecast

interface RemoteSource {
    suspend fun getCurrentWeather(latitude:Double,longitude:Double):Forecast
}