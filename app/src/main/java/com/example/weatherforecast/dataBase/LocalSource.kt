package com.example.weatherforecast.dataBase

import com.example.weatherforecast.MyLocations

interface LocalSource {
    suspend fun getStoredLocations():List<MyLocations>
    suspend fun insert(data:MyLocations)
    suspend fun delete(data: MyLocations)
}