package com.example.weatherforecast.generalRepository

import com.example.weatherforecast.Forecast
import com.example.weatherforecast.network.RemoteSource

class Repository private constructor(var RS: RemoteSource):RepositoryInterface {
    companion object {
        @Volatile
        private var INSTANCE : Repository?=null

        fun getInstance(RS: RemoteSource):Repository {
            return INSTANCE?: synchronized(this){
                val temp = Repository(RS)
                INSTANCE=temp
                temp
            }
        }

    }
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Forecast {
        return RS.getCurrentWeather(latitude,longitude)
    }
}