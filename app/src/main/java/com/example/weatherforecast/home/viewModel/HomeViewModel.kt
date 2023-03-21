package com.example.weatherforecast.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.generalRepository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repo:RepositoryInterface):ViewModel() {
   private var myWeather:MutableLiveData<Forecast> = MutableLiveData<Forecast>()
    val finalWeather: LiveData<Forecast> = myWeather
    init {
         getMyWeatherStatus()
    }
    private fun getMyWeatherStatus(){
        viewModelScope.launch(Dispatchers.IO) {
              myWeather.postValue(repo.getCurrentWeather())
        }
    }
}