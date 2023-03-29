package com.example.weatherforecast.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.MyLocations

@Database(entities = arrayOf(MyLocations::class), version = 2 )
abstract class WeatherDataBase : RoomDatabase(){
    abstract fun getWeatherDao(): WeatherDao
    companion object{
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance (ctx: Context): WeatherDataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDataBase::class.java, "weather_database_v2")
                    .build()
                INSTANCE = instance
// return instance
                instance }
        }
    }
}
