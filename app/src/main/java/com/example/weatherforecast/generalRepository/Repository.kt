package com.example.weatherforecast.generalRepository

import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.dataBase.LocalSource
import com.example.weatherforecast.network.RemoteSource

class Repository private constructor(var RS: RemoteSource,var LS:LocalSource):RepositoryInterface {
    companion object {
        @Volatile
        private var INSTANCE : Repository?=null

        fun getInstance(RS: RemoteSource,LS:LocalSource):Repository {
            return INSTANCE?: synchronized(this){
                val temp = Repository(RS,LS)
                INSTANCE=temp
                temp
            }
        }

    }
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Forecast {
        return RS.getCurrentWeather(latitude,longitude)
    }

    override suspend fun getStoredLocations(): List<MyLocations> {
       return LS.getStoredLocations()
    }

    override suspend fun insert(data: MyLocations) {
        LS.insert(data)
    }

    override suspend fun delete(data: MyLocations) {
        LS.delete(data)
    }
}