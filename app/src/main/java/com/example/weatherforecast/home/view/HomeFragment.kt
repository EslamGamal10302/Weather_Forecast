package com.example.weatherforecast.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.Constant
import com.example.weatherforecast.R
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.home.viewModel.HomeViewModel
import com.example.weatherforecast.home.viewModel.HomeViewModelFactory
import com.example.weatherforecast.network.GpsLocation
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


    lateinit var factory: HomeViewModelFactory
    lateinit var viewModel: HomeViewModel
    lateinit var loading: ProgressDialog


    override fun onAttach(context: Context) {
        super.onAttach(context)
        loading = ProgressDialog(context)
        loading.setMessage(getString(R.string.loading))
    }

    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
        (activity as AppCompatActivity?)?.supportActionBar?.title = requireActivity().getString(R.string.home)
        //medium_purple


        //  getLastLocation() call here
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val status = sharedPref.getString("location","")
        val units = sharedPref.getString("units","metric")
        val language = sharedPref.getString("language","en")
        Log.i("milad","$units")
        Log.i("milad","$language")

        factory = HomeViewModelFactory(
            GpsLocation(requireContext()),
            requireContext(),
            Repository.getInstance(WeatherClient.getInstance(),LocalRepository.getInstance(requireContext()))
        )
        viewModel = ViewModelProvider(requireActivity(), factory).get(HomeViewModel::class.java)
        when (status){
            "map"-> {
                loading.dismiss()
                Toast.makeText(requireContext(), requireContext().getString(R.string.choose_location),Toast.LENGTH_LONG).show()
                var action = HomeFragmentDirections.actionHomeFragmentToMapsFragment("home")
                Navigation.findNavController(requireView()).navigate(action)
            }
            "gps" ->{
                loading.show()
                viewModel.getMyGpsLocation(language!!,units!!)
                Log.i("milad","here gps")
            }
            "mapResult"->{
               var latitude = sharedPref.getFloat("lat",0f).toDouble()
                var longitude = sharedPref.getFloat("lon",0f).toDouble()
                viewModel.getMyWeatherStatus(latitude,longitude,language!!,units!!)
                Log.i("milad","here gps modified")

            }
        }

       // viewModel.getMyGpsLocation()


        viewModel.finalWeather.observe(viewLifecycleOwner) {
            Log.i("test", "call view model")
            loading.dismiss()
            viewModel.addCurrentLocationToDataBase(it)
            binding.areaTxt.visibility = View.VISIBLE
            binding.areaTxt.text = it.timezone


            var simpleDate = SimpleDateFormat("dd MMM")
            var currentDate = simpleDate.format(it.current.dt * 1000L)
            binding.dateTxt.visibility = View.VISIBLE
            binding.dateTxt.text = currentDate.toString()


            binding.cardView.visibility=View.VISIBLE
            binding.weatherTempTxt.visibility=View.VISIBLE
            binding.weatherStatusTxt.visibility=View.VISIBLE
            binding.weatherIconImg.visibility=View.VISIBLE
            binding.weatherStatusTxt.text = it.current.weather.get(0).description
            var temp = it.current.temp
            var intTemp = Math.ceil(temp).toInt()
           // var tempCelucis = "$intTemp°C"
            var finalTemp = if(units.equals("standard")) "$intTemp°K" else if (units.equals("metric")) "$intTemp°C" else "$intTemp°F"
            binding.weatherTempTxt.text = finalTemp
            val url = "https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png"
            Glide.with(requireContext()).load(url).into(binding.weatherIconImg)

            binding.detailsCard.visibility=View.VISIBLE
            binding.pressureTxt.text = it.current.pressure.toString()
            binding.humidityTxt.text = it.current.humidity.toString()
            binding.windTxt.text = it.current.wind_speed.toString()
            binding.cloudTxt.text = it.current.clouds.toString()
            binding.ultraVioletTxt.text = it.current.uvi.toString()
            binding.visibilityTxt.text = it.current.visibility.toString()

            binding.weeklyWeatherRv.visibility=View.VISIBLE
            binding.hourlyWeatherRv.visibility=View.VISIBLE
            var manger = LinearLayoutManager(requireContext())
            manger.orientation = RecyclerView.HORIZONTAL
            binding.hourlyWeatherRv.layoutManager = manger
            binding.hourlyWeatherRv.adapter = DayAdapter(requireContext(), it.hourly)
            var mangerVertical = LinearLayoutManager(requireContext())
            mangerVertical.orientation = RecyclerView.VERTICAL
            binding.weeklyWeatherRv.layoutManager = mangerVertical
            binding.weeklyWeatherRv.adapter = WeekAdapter(requireContext(), it.daily)

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner
        return binding.root
    }


}