package com.example.weatherforecast.alerts.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.generalRepository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsViewModel (private val repo: RepositoryInterface): ViewModel() {
    private var myAlerts: MutableLiveData<List<MyUserAlert>> = MutableLiveData<List<MyUserAlert>>()
    val finalAlerts: LiveData<List<MyUserAlert>> = myAlerts

    init {
        getAllAlerts()
    }
    fun getAllAlerts(){
        viewModelScope.launch(Dispatchers.IO) {
            myAlerts.postValue(repo.getStoredAlerts())
        }
    }

    fun addAlert(data : MyUserAlert){
         viewModelScope.launch (Dispatchers.IO){
             repo.insertAlert(data)
             getAllAlerts()
         }
    }

    fun deleteAlert(data : MyUserAlert){
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteAlert(data)
            getAllAlerts()
        }
    }


}