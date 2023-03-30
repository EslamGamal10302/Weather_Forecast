package com.example.weatherforecast.home.viewModel

import android.content.Context
import android.telephony.CarrierConfigManager.Gps
import android.util.Log
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
     fun getMyWeatherStatus(latitude:Double,longitude:Double,language:String,units:String){
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("milad","hit api ")
            myWeather.postValue(repo.getCurrentWeather(latitude,longitude,language,units))
        }
    }
     fun getMyGpsLocation(language:String,units:String){
         myGps.mydata =MutableLiveData<Pair<Double,Double>>()
        myGps.getLastLocation()
        myGps.mydata.observe(context as LifecycleOwner){
            getMyWeatherStatus(it.first,it.second,language,units)
        }
    }

}