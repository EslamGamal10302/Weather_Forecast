package com.example.weatherforecast.dataBase

import androidx.room.*
import com.example.weatherforecast.MyLocations
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM locations")
     fun getAllFavorites(): Flow<List <MyLocations>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favLocation: MyLocations)
    @Delete
    suspend fun delete(favLocation: MyLocations)
}





