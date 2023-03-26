package com.example.weatherforecast.favorites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherforecast.generalRepository.RepositoryInterface


class FavoritesViewModelFactory(private val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)){
            FavoritesViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("View Model Class Not Found")
        }
    }
}