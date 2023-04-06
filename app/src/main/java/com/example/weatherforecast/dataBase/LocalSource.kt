package com.example.weatherforecast.dataBase

import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert

interface LocalSource {
    suspend fun getStoredLocations():List<MyLocations>
    suspend fun insert(data:MyLocations)
    suspend fun delete(data: MyLocations)

    suspend fun getStoredAlerts():List<MyUserAlert>
    suspend fun insertAlert(data:MyUserAlert)
    suspend fun deleteAlert(data: MyUserAlert)


    suspend fun getMyBackupLocation():List<Forecast>
    suspend fun insertMyCurrentLocation(data:Forecast)
    suspend fun deleteMyCurrentLocation(data: Forecast)



}