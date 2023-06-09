package com.example.weatherforecast

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


object Constant {
    const val API_KEY: String = "a2ea0afa8140452e429af957b34af036"
    const val My_LOCATION_PERMISSION_ID = 5005
}

@Entity(tableName = "locations")
data class MyLocations constructor(@PrimaryKey var latitude: Double, var longitude: Double) :
    java.io.Serializable


@Entity(tableName = "userAlerts")
data class MyUserAlert(
    var dateFrom: Long,
    var dateTo: Long,
    var timeFrom: Long,
    var timeTo: Long,
    var type: String,
    var event: String,
    @PrimaryKey var id: Int
) {
    constructor() : this(0, 0, 0, 0, "Heat Advisory", "Heat Advisory", 0)
}

@Entity(tableName = "backup")
data class Forecast(

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
    var timezone_offset: Int,
    var alerts: List<Alerts>
) {
    constructor() : this(Current(), listOf(), listOf(), 0.00, 0.00, listOf(), "", 0, listOf())
}

data class Alerts(
    var senderName: String? = null,
    var event: String? = null,
    var start: Int? = null,
    var end: Int? = null,
    var description: String? = null,
    var tags: ArrayList<String> = arrayListOf()
) {
    constructor() : this("", "", 0, 0, "")
}

data class Current(
    var clouds: Int,
    var dew_point: Double,
    var dt: Int,
    var feels_like: Double,
    var humidity: Int,
    var pressure: Int,
    @Ignore
    var rain: Rain,
    var sunrise: Int,
    var sunset: Int,
    var temp: Double,
    var uvi: Double,
    var visibility: Int,
    var weather: List<Weather>,
    var wind_deg: Int,
    var wind_speed: Double
) {
    constructor() : this(
        0, 0.0, 0, 0.0, 0, 0, Rain(), 0, 0, 0.0, 0.0, 0, listOf(),
        0,
        0.0
    )
}

data class Daily(
    var clouds: Int,
    var dew_point: Double,
    val dt: Int,
    val feels_like: FeelsLike,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val snow: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val uvi: Double,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
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
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: RainX,
    val snow: Snow,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)

data class Hours(
    val tempretur: Int,
    val icon: Int,
    val hour: String
)

data class Minutely(
    val dt: Int,
    val precipitation: Double
)

data class Rain(
    val `1h`: Double
) {
    constructor() : this(0.0)
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
) {
    constructor() : this("", "", 0, "")
}


object NetworkConnection {
    fun getConnectivity(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return if (activeNetwork != null) {
            true
        } else {
            false
        }
    }
}

sealed class ApiState {
    class Success(val data: Forecast) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}

class MyWeatherIcons {
    companion object {
        var mapIcon = HashMap<String, Int>()

        init {
            mapIcon["01d"] = R.drawable.d01
            mapIcon["02d"] = R.drawable.d02
            mapIcon["03d"] = R.drawable.d03
            mapIcon["04d"] = R.drawable.d04
            mapIcon["09d"] = R.drawable.d09
            mapIcon["10d"] = R.drawable.d10
            mapIcon["11d"] = R.drawable.d11
            mapIcon["13d"] = R.drawable.d13
            mapIcon["50d"] = R.drawable.d50

            mapIcon["01n"] = R.drawable.n01
            mapIcon["02n"] = R.drawable.n02
            mapIcon["03n"] = R.drawable.n03
            mapIcon["04n"] = R.drawable.n04
            mapIcon["09n"] = R.drawable.n09
            mapIcon["10n"] = R.drawable.n10
            mapIcon["11n"] = R.drawable.n11
            mapIcon["13n"] = R.drawable.n13
            mapIcon["50n"] = R.drawable.n50
        }

    }
}