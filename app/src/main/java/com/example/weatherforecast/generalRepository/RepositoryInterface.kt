package com.example.weatherforecast.generalRepository

import com.example.weatherforecast.dataBase.LocalSource
import com.example.weatherforecast.network.RemoteSource

interface RepositoryInterface : RemoteSource,LocalSource{
}