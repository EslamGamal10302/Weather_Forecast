package com.example.weatherforecast.alerts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherforecast.favorites.viewModel.FavoritesViewModel
import com.example.weatherforecast.generalRepository.RepositoryInterface

class AlertsViewModelFactory (private val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(AlertsViewModel::class.java)){
            AlertsViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("View Model Class Not Found")
        }
    }
}