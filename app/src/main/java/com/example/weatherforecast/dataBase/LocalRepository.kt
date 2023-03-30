package com.example.weatherforecast.dataBase

import android.content.Context
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyLocations

class LocalRepository private constructor(val context: Context):LocalSource {
    private val weatherDAO :WeatherDao

    private val backupDAO :BackupDao


    init {
        val db: WeatherDataBase = WeatherDataBase.getInstance(context.applicationContext)
        weatherDAO = db.getWeatherDao()
        backupDAO=db.getBackupDao()

    }

    companion object {
        @Volatile
        private var INSTANCE : LocalRepository?=null

        fun getInstance(context: Context): LocalRepository {
            return INSTANCE?: synchronized(this){
                val temp = LocalRepository(context)
                INSTANCE=temp
                temp
            }
        }

    }


    override suspend fun getStoredLocations(): List<MyLocations> {
        return weatherDAO.getAllFavorites()
    }

    override suspend fun insert(data: MyLocations) {
        weatherDAO.insert(data)
    }

    override suspend fun delete(data: MyLocations) {
        weatherDAO.delete(data)
    }

    override suspend fun getMyBackupLocation(): List<Forecast> {
        return backupDAO.getBackupForMyLocation()
    }

    override suspend fun insertMyCurrentLocation(data: Forecast) {
        backupDAO.insert(data)
    }

    override suspend fun deleteMyCurrentLocation(data: Forecast) {
        backupDAO.delete(data)
    }
}


