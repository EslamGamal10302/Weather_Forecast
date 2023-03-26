package com.example.weatherforecast.favorites.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.generalRepository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel (private val repo: RepositoryInterface): ViewModel() {
    private var myWeather: MutableLiveData<List<MyLocations>> = MutableLiveData<List<MyLocations>>()
    val finalWeather: LiveData<List<MyLocations>> = myWeather
    init {
    }

    fun getAllFavLocations(){
        viewModelScope.launch(Dispatchers.IO) {
           myWeather.postValue(repo.getStoredLocations())
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