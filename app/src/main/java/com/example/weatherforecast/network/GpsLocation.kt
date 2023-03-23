package com.example.weatherforecast.network

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.Constant
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class GpsLocation (var context:Context) {
    var  myFusedLocationClinet= LocationServices.getFusedLocationProviderClient(context)
    var mydata: MutableLiveData<Pair<Double,Double>> = MutableLiveData<Pair<Double,Double>>()
    @SuppressLint("MissingPermission")
     fun getLastLocation() {
        if (checkPermission()){
            if(isLocationEnabled()){
                requestNewLocation()

            } else{
                Toast.makeText(context,"please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }

        }else {
            requestPermission()
        }

    }
    private fun checkPermission():Boolean{
        return  (ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED)
                ||
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), Constant.My_LOCATION_PERMISSION_ID)
    }
    private  fun isLocationEnabled():Boolean{

        val locationManger : LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManger.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
    @SuppressLint("MissingPermission", "SuspiciousIndentation")
    private fun requestNewLocation() {
        val myLocationRequest = com.google.android.gms.location.LocationRequest()
        myLocationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        myLocationRequest.setInterval(0)
        myFusedLocationClinet.requestLocationUpdates(myLocationRequest, myLocationCallBack, Looper.myLooper())
    }
    private val myLocationCallBack : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            val result: Location = p0!!.lastLocation
            result.latitude.toString()
            result.longitude.toString()
            Log.i("test", "${result.latitude}${result.longitude}")
            mydata.postValue(Pair(result.latitude,result.longitude))
            stopObserve()

        }
    }

    private fun stopObserve() {
        myFusedLocationClinet.removeLocationUpdates(myLocationCallBack)
    }
}