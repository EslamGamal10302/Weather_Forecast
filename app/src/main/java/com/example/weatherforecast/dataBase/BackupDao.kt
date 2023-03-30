package com.example.weatherforecast.dataBase


import androidx.room.*
import com.example.weatherforecast.Forecast
@Dao
interface BackupDao {
    @Query("SELECT * FROM backup")
    suspend fun getBackupForMyLocation(): List<Forecast>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newLocation: Forecast)
    @Delete
    suspend fun delete(Location: Forecast)
}