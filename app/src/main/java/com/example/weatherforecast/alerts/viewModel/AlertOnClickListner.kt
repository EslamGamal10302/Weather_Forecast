package com.example.weatherforecast.alerts.viewModel

import com.example.weatherforecast.MyUserAlert

interface AlertOnClickListner {
    fun onDialogSave(data : MyUserAlert)
    fun deleteAlert(data : MyUserAlert)
}