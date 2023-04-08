package com.example.weatherforecast.network

import com.example.weatherforecast.Forecast
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClient private constructor():RemoteSource {
    lateinit var myData : Forecast

    companion object{
        var repo = WeatherClient()
        fun getInstance(): WeatherClient {
            if (repo == null) {
                repo = WeatherClient()
            }
            return repo
        }
    }

    object RetrofitHelper{
        val gson = GsonBuilder().create()
        val myRetrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    object MyApi{
        val service:WeatherService by lazy {
            RetrofitHelper.myRetrofit.create(WeatherService::class.java)
        }

    }
    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ): Forecast{
        val myResponser = MyApi.service.getResponse(lat = latitude, lon = longitude, lang = language, units = units)
        if (myResponser.isSuccessful) {
            myData  = myResponser.body()!!
        }

        return myData
    }
}