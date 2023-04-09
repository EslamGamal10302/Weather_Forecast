package com.example.weatherforecast.generalRepository

import com.example.weatherforecast.Forecast
import com.example.weatherforecast.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FakeRemoteSource:RemoteSource {
    var myData =Forecast()
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, language: String, units: String
    ): Flow<Forecast> {
        //hit api call


        var flowDate= flow {
            emit(myData)
        }
        return flowDate
    }
}