package com.example.weatherforecast.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable.Orientation
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.pm.ActivityInfoCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.Constant
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.home.viewModel.HomeViewModel
import com.example.weatherforecast.home.viewModel.HomeViewModelFactory
import com.example.weatherforecast.network.WeatherClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding


    lateinit var factory:HomeViewModelFactory
    lateinit var viewModel:HomeViewModel
    lateinit var loading:ProgressDialog
    override fun onAttach(context: Context) {
        super.onAttach(context)
        loading= ProgressDialog(context)
        loading.setMessage("Loading.....")
        loading.show()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
        (activity as AppCompatActivity?)?.supportActionBar?.title="Home"
        getLastLocation()

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermission()){
            if(isLocationEnabled()){
                requestNewLocation()

            } else{
                Toast.makeText(requireContext(),"please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

        }else {
            requestPermission()
        }

    }

   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==Constant.My_LOCATION_PERMISSION_ID){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }

    }  */

    private fun checkPermission():Boolean{
        return  (ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED)
                ||
                (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),Constant.My_LOCATION_PERMISSION_ID)
    }
    private  fun isLocationEnabled():Boolean{

        val locationManger : LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManger.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
    @SuppressLint("MissingPermission", "SuspiciousIndentation")
    private fun requestNewLocation() {
        val myLocationRequest = com.google.android.gms.location.LocationRequest()
        myLocationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        myLocationRequest.setInterval(0)
       var  myFusedLocationClinet= LocationServices.getFusedLocationProviderClient(requireContext())
        myFusedLocationClinet.requestLocationUpdates(myLocationRequest, myLocationCallBack, Looper.myLooper())
    }
    private val myLocationCallBack : LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            val result : Location = p0!!.lastLocation
            result.latitude.toString()
            result.longitude.toString()
            Log.i("test","${result.latitude}${result.longitude}")

            factory= HomeViewModelFactory(Repository.getInstance(WeatherClient.getInstance()),result.latitude,result.longitude)
            viewModel=ViewModelProvider(requireActivity(),factory).get(HomeViewModel::class.java)
            Log.i("test","before view model")
            viewModel.finalWeather.observe(viewLifecycleOwner){
                Log.i("test","call view model")
                loading.dismiss()
                binding.areaTxt.text=it.timezone


                var simpleDate = SimpleDateFormat("dd MMM")
                var currentDate = simpleDate.format(it.current.dt*1000L)
                binding.dateTxt.text=currentDate.toString()



                binding.weatherStatusTxt.text=it.current.weather.get(0).description
                var temp=it.current.temp
                var intTemp = Math.ceil(temp).toInt()
                var tempCelucis= "$intTemp°C"
                binding.weatherTempTxt.text=tempCelucis
                val url = "https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png"
                Glide.with(requireContext()).load(url).into(binding.weatherIconImg)


                binding.pressureTxt.text=it.current.pressure.toString()
                binding.humidityTxt.text=it.current.humidity.toString()
                binding.windTxt.text=it.current.wind_speed.toString()
                binding.cloudTxt.text=it.current.clouds.toString()
                binding.ultraVioletTxt.text=it.current.uvi.toString()
                binding.visibilityTxt.text=it.current.visibility.toString()


                var manger=LinearLayoutManager(requireContext())
                manger.orientation=RecyclerView.HORIZONTAL
                binding.hourlyWeatherRv.layoutManager=manger
                binding.hourlyWeatherRv.adapter=DayAdapter(requireContext(),it.hourly)
                var mangerVertical=LinearLayoutManager(requireContext())
                mangerVertical.orientation=RecyclerView.VERTICAL
                binding.weeklyWeatherRv.layoutManager=mangerVertical
                binding.weeklyWeatherRv.adapter=WeekAdapter(requireContext(),it.daily)

            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        binding.lifecycleOwner
        return binding.root
    }


}