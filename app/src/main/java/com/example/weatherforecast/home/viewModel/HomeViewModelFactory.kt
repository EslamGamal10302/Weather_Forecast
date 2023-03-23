package com.example.weatherforecast.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherforecast.generalRepository.RepositoryInterface

class HomeViewModelFactory (private val repo:RepositoryInterface,private val latitude:Double,private val longitude:Double):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repo,latitude,longitude) as T
        }else{
            throw java.lang.IllegalArgumentException("View Model Class Not Found")
        }
    }
}