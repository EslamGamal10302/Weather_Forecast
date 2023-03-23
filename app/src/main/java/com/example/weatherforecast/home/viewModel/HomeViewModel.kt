package com.example.weatherforecast.home.viewModel

import android.content.Context
import android.telephony.CarrierConfigManager.Gps
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.generalRepository.RepositoryInterface
import com.example.weatherforecast.network.GpsLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val myGps: GpsLocation,private val context: Context, private val repo:RepositoryInterface):ViewModel() {
   private var myWeather:MutableLiveData<Forecast> = MutableLiveData<Forecast>()
    val finalWeather: LiveData<Forecast> = myWeather
    init {

    }
    private fun getMyWeatherStatus(latitude:Double,longitude:Double){
        viewModelScope.launch(Dispatchers.IO) {
            myWeather.postValue(repo.getCurrentWeather(latitude,longitude))
        }
    }
     fun getMyGpsLocation(){
        myGps.getLastLocation()
        myGps.mydata.observe(context as LifecycleOwner){
            getMyWeatherStatus(it.first,it.second)
        }
    }
}