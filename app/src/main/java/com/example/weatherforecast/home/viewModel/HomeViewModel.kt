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
            var locationData=repo.getCurrentWeather(latitude,longitude,language,units)
            myWeather.postValue(locationData)
            addCurrentLocationToDataBase(locationData)
        }
    }
     fun getMyGpsLocation(language:String,units:String){
         myGps.mydata =MutableLiveData<Pair<Double,Double>>()
       // myGps.getLastLocation()
         myGps.requestNewLocation()
        myGps.mydata.observe(context as LifecycleOwner){
            getMyWeatherStatus(it.first,it.second,language,units)
            val sharedPref = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putFloat("lat",it.first.toFloat())
            editor.putFloat("lon",it.second.toFloat())
            editor.apply()
        }
    }

    fun addCurrentLocationToDataBase(data:Forecast){
        viewModelScope.launch {
            repo.insertMyCurrentLocation(data)
        }
    }

    fun getMyBackupLocation(){
        viewModelScope.launch {
            var backupDate = repo.getMyBackupLocation()
            if(!backupDate.isEmpty()){
                var size = backupDate.size
                myWeather.postValue(backupDate.get(size-1))
            }
        }
    }

}