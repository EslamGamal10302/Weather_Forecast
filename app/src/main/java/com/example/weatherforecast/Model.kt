package com.example.weatherforecast

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


object Constant{
   // const val API_KEY:String="416c3f7d60f73a4f8f76c658c93cf3b7"
   const val API_KEY:String="a2ea0afa8140452e429af957b34af036"
    const val My_LOCATION_PERMISSION_ID = 5005
}

@Entity(tableName = "locations")
data class MyLocations constructor(@PrimaryKey var latitude:Double, var longitude:Double):java.io.Serializable




@Entity(tableName = "backup")
data class Forecast(
    // val place: String,
    //val alerts: List<Alert>,
    @Embedded
    var current: Current,
    var daily: List<Daily>,
    var hourly: List<Hourly>,
    var lat: Double,
    var lon: Double,
    @Ignore
    var minutely: List<Minutely>,
    @PrimaryKey
    var timezone: String,
    var timezone_offset: Int
){
    constructor():this(Current(), listOf(), listOf(),0.00,0.00, listOf(),"",0)
}
data class Current(
    var clouds: Int,//need
    var dew_point: Double,
    var dt: Int,//need
    var feels_like: Double,
    var humidity: Int,//need
    var pressure: Int,//need
    @Ignore
    var rain: Rain,
    var sunrise: Int,
    var sunset: Int,
    var temp: Double,//need
    var uvi: Double,
    var visibility: Int,
    var weather: List<Weather>,
    var wind_deg: Int,
    var wind_speed: Double//need
) {
    constructor():this(0,0.0,0,0.0,0,0, Rain(),0,0,0.0,0.0,0, listOf(),
    0,
    0.0)
}
data class Daily(
    var clouds: Int,//need
    var dew_point: Double,
    val dt: Int,
    val feels_like: FeelsLike,
    val humidity: Int,//need
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,//need
    val rain: Double,
    val snow: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,//need
    val uvi: Double,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double//need
)
data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)
data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,//need
    val feels_like: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: RainX,
    val snow: Snow,
    val temp: Double,//need
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)
data class Hours(val tempretur: Int,
                 val icon: Int,
                 val hour: String)
data class Minutely(
    val dt: Int,
    val precipitation: Double
)
data class Rain(
    val `1h`: Double
){
    constructor():this(0.0)
}
data class RainX(
    val `1h`: Double
)
data class Snow(
    val `1h`: Double
)
data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
)
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
){
    constructor():this("","",0,"")
}

