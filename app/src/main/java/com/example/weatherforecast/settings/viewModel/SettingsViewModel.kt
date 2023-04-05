package com.example.weatherforecast.settings.viewModel

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.generalRepository.RepositoryInterface
import com.example.weatherforecast.network.WeatherClient

class SettingsViewModel : ViewModel(){
    private var myWeather: MutableLiveData<Forecast> = MutableLiveData<Forecast>()
    val finalWeather: LiveData<Forecast> = myWeather
    init {

    }
}