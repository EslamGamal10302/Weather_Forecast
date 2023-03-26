package com.example.weatherforecast.dataBase

import androidx.room.*
import com.example.weatherforecast.MyLocations

@Dao
interface WeatherDao {
    @Query("SELECT * FROM locations")
    suspend fun getAllFavorites(): List <MyLocations>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favLocation: MyLocations)
    @Delete
    suspend fun delete(favLocation: MyLocations)
}





