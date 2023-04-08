package com.example.weatherforecast.dataBase

import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert
import kotlinx.coroutines.flow.Flow

interface LocalSource {
     fun getStoredLocations():Flow<List<MyLocations>>
    suspend fun insert(data:MyLocations)
    suspend fun delete(data: MyLocations)

    fun getStoredAlerts():Flow<List<MyUserAlert>>
    suspend fun insertAlert(data:MyUserAlert)
    suspend fun deleteAlert(data: MyUserAlert)


     fun getMyBackupLocation(): Flow<List<Forecast>>
    suspend fun insertMyCurrentLocation(data:Forecast)
    suspend fun deleteMyCurrentLocation(data: Forecast)



}