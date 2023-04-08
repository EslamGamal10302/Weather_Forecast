package com.example.weatherforecast.dataBase

import androidx.room.*
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {
    @Query("SELECT * FROM userAlerts")
    fun getAllAlerts(): Flow<List<MyUserAlert>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alert: MyUserAlert)
    @Delete
    suspend fun delete(alert: MyUserAlert)
}