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
    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ): Forecast {
        return RS.getCurrentWeather(latitude,longitude,language,units)
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

    override suspend fun getMyBackupLocation(): List<Forecast> {
        return LS.getMyBackupLocation()
    }

    override suspend fun insertMyCurrentLocation(data: Forecast) {
        LS.insertMyCurrentLocation(data)
    }

    override suspend fun deleteMyCurrentLocation(data: Forecast) {
        LS.deleteMyCurrentLocation(data)
    }
}