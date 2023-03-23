package com.example.weatherforecast.home.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherforecast.generalRepository.RepositoryInterface
import com.example.weatherforecast.network.GpsLocation

class HomeViewModelFactory (private val myGps: GpsLocation, private val context:Context, private val repo:RepositoryInterface):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(myGps,context,repo) as T
        }else{
            throw java.lang.IllegalArgumentException("View Model Class Not Found")
        }
    }
}