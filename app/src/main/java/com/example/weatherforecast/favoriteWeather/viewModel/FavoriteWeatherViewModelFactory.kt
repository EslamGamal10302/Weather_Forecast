package com.example.weatherforecast.favoriteWeather.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherforecast.generalRepository.RepositoryInterface


class FavoriteWeatherViewModelFactory(private val repo: RepositoryInterface):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(FavoriteWeatherViewModel::class.java)){
            FavoriteWeatherViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("View Model Class Not Found")
        }
    }
}



