package com.example.weatherforecast.favoriteWeather.viewModel

import android.content.Context
import androidx.lifecycle.*
import com.example.weatherforecast.ApiState
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.generalRepository.RepositoryInterface
import com.example.weatherforecast.network.GpsLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteWeatherViewModel(private val repo: RepositoryInterface):ViewModel() {
    /*private var myWeather: MutableLiveData<Forecast> = MutableLiveData<Forecast>()
    val finalWeather: LiveData<Forecast> = myWeather*/
    private val myWeather: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val finalWeather = myWeather.asStateFlow()
    fun getMyWeatherStatus(latitude:Double,longitude:Double,language:String,units:String){
        viewModelScope.launch(Dispatchers.IO) {
          //  myWeather.postValue(repo.getCurrentWeather(latitude,longitude,language,units))
            repo.getCurrentWeather(latitude,longitude,language,units)
                .catch {
                    myWeather.value=ApiState.Failure(it)
                }
                .collect(){
                    myWeather.value=ApiState.Success(it)
            }
        }
    }
}
