package com.example.weatherforecast.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.Converters
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert

@Database(entities = arrayOf(MyLocations::class, Forecast::class,MyUserAlert::class), version = 7, exportSchema = false )
@TypeConverters(Converters::class)
abstract class WeatherDataBase : RoomDatabase(){
    abstract fun getWeatherDao(): WeatherDao

    abstract fun getBackupDao(): BackupDao

    abstract fun getAlertDao(): AlertsDao

    companion object{
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance (ctx: Context): WeatherDataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDataBase::class.java, "weather_database_v5")
                    .build()
                INSTANCE = instance
// return instance
                instance }
        }
    }
}

