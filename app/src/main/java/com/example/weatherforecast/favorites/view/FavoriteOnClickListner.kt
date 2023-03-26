package com.example.weatherforecast.favorites.view

import com.example.weatherforecast.MyLocations

interface FavoriteOnClickListner {
    fun removeFromFavorite(data:MyLocations)
    fun showFavoriteLocationWeather(latitude:Double,longitude:Double)
}