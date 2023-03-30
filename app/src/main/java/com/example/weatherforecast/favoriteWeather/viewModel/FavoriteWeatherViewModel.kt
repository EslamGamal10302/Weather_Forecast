package com.example.weatherforecast.favoriteWeather.viewModel

import android.content.Context
import androidx.lifecycle.*
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.generalRepository.RepositoryInterface
import com.example.weatherforecast.network.GpsLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteWeatherViewModel(private val repo: RepositoryInterface):ViewModel() {
    private var myWeather: MutableLiveData<Forecast> = MutableLiveData<Forecast>()
    val finalWeather: LiveData<Forecast> = myWeather
    fun getMyWeatherStatus(latitude:Double,longitude:Double,language:String,units:String){
        viewModelScope.launch(Dispatchers.IO) {
            myWeather.postValue(repo.getCurrentWeather(latitude,longitude,language,units))
        }
    }
}
