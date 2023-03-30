package com.example.weatherforecast.network

import com.example.weatherforecast.Constant
import com.example.weatherforecast.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")
    suspend fun getResponse(@Query("lat") lat:Double,
                            @Query("lon") lon:Double,
                            @Query("units")units:String,
                            @Query("lang")lang:String,
                            @Query("exclude")exclude:String="minutely",
                            @Query("appid")appid:String=Constant.API_KEY): Response<Forecast>
}