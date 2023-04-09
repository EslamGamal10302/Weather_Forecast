package com.example.weatherforecast.generalRepository

import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.dataBase.LocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalSource:LocalSource {
    var backupLocation:MutableList<Forecast> = mutableListOf()
    override fun getStoredLocations(): Flow<List<MyLocations>> {

        TODO("Not yet implemented")
    }

    override suspend fun insert(data: MyLocations) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(data: MyLocations) {
        TODO("Not yet implemented")
    }

    override fun getStoredAlerts(): Flow<List<MyUserAlert>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(data: MyUserAlert) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(data: MyUserAlert) {
        TODO("Not yet implemented")
    }

    override fun getMyBackupLocation(): Flow<List<Forecast>> {
        val flowData= flow {
            val storedLocatios=backupLocation.toList()
            if(!storedLocatios.isEmpty()){
                emit(storedLocatios)
            }
        }
        return  flowData
    }

    override suspend fun insertMyCurrentLocation(data: Forecast) {
        backupLocation.add(data)
    }

    override suspend fun deleteMyCurrentLocation(data: Forecast) {
        backupLocation.remove(data)
    }
}