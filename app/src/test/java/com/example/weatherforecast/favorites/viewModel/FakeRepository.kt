package com.example.weatherforecast.favorites.viewModel

import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.generalRepository.RepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository: RepositoryInterface {
    var favLocation:MutableList<MyLocations> = mutableListOf()

    var favAlerts:MutableList<MyUserAlert> = mutableListOf()


    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ): Flow<Forecast> {
        TODO("Not yet implemented")
    }

    override fun getStoredLocations(): Flow<List<MyLocations>> {
        val flowData= flow {
            val storedLocatios=favLocation.toList()
            if(!storedLocatios.isEmpty()){
                emit(storedLocatios)
            }
        }
        return  flowData
    }

    override suspend fun insert(data: MyLocations) {
        favLocation.add(data)
    }

    override suspend fun delete(data: MyLocations) {
        favLocation.remove(data)
    }

    override fun getStoredAlerts(): Flow<List<MyUserAlert>> {
        val flowData= flow {
            val storedLocatios=favAlerts.toList()
            if(!storedLocatios.isEmpty()){
                emit(storedLocatios)
            }
        }
        return  flowData
    }

    override suspend fun insertAlert(data: MyUserAlert) {
        favAlerts.add(data)
    }

    override suspend fun deleteAlert(data: MyUserAlert) {
        favAlerts.remove(data)
    }

    override fun getMyBackupLocation(): Flow<List<Forecast>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertMyCurrentLocation(data: Forecast) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMyCurrentLocation(data: Forecast) {
        TODO("Not yet implemented")
    }
}