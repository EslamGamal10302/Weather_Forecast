package com.example.weatherforecast.network

import com.example.weatherforecast.Forecast
import com.google.gson.GsonBuilder
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
    override suspend fun getCurrentWeather(): Forecast {
        val myResponser = MyApi.service.getResponse(33.44,-94.04)
        if (myResponser.isSuccessful) {
            myData  = myResponser.body()!!
        }
        return myData
    }
}