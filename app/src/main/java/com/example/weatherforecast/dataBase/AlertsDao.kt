package com.example.weatherforecast.dataBase

import androidx.room.*
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert

@Dao
interface AlertsDao {
    @Query("SELECT * FROM userAlerts")
    suspend fun getAllAlerts(): List <MyUserAlert>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alert: MyUserAlert)
    @Delete
    suspend fun delete(alert: MyUserAlert)
}