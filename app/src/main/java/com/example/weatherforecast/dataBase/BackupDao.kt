package com.example.weatherforecast.dataBase


import androidx.room.*
import com.example.weatherforecast.Forecast
import kotlinx.coroutines.flow.Flow

@Dao
interface BackupDao {
    @Query("SELECT * FROM backup")
     fun getBackupForMyLocation(): Flow<List<Forecast>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newLocation: Forecast)
    @Delete
    suspend fun delete(Location: Forecast)
}