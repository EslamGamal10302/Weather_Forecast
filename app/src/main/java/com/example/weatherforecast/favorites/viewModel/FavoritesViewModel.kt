package com.example.weatherforecast.favorites.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.NetworkConnection
import com.example.weatherforecast.generalRepository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoritesViewModel (private val repo: RepositoryInterface): ViewModel() {
    /*private var myWeather: MutableLiveData<List<MyLocations>> = MutableLiveData<List<MyLocations>>()
    val finalWeather: LiveData<List<MyLocations>> = myWeather*/

    private val myWeather: MutableStateFlow<List<MyLocations>> = MutableStateFlow(listOf())
    val finalWeather = myWeather.asStateFlow()
    init {
    }

    fun getAllFavLocations(){
        viewModelScope.launch(Dispatchers.IO) {
          // myWeather.postValue(repo.getStoredLocations())
            repo.getStoredLocations().collect{
                myWeather.value=it
            }
        }
    }




    fun addToFavorites(data:MyLocations){
        viewModelScope.launch (Dispatchers.IO){
            repo.insert(data)
             getAllFavLocations()
        }
    }

    fun deleteFromFavorites(data:MyLocations){
        viewModelScope.launch (Dispatchers.IO){
            repo.delete(data)
            getAllFavLocations()
        }
    }
}