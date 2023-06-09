package com.example.weatherforecast.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.os.RemoteException
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.example.weatherforecast.*
import com.example.weatherforecast.Constant.My_LOCATION_PERMISSION_ID
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding


    lateinit var factory: HomeViewModelFactory
    lateinit var viewModel: HomeViewModel
    lateinit var loading: ProgressDialog
    lateinit var layout: ConstraintLayout
    lateinit var sharedPref: SharedPreferences
    lateinit var status: String
    lateinit var units: String
    lateinit var language: String
    var requestPermissionStatus = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        loading = ProgressDialog(context)
        loading.setMessage(getString(R.string.loading))
        Log.i("lifecylce", "attach")
    }

    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.home)


        sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        status = sharedPref.getString("location", "").toString()
        units = sharedPref.getString("units", "metric").toString()
        language = sharedPref.getString("language", "en").toString()


        factory = HomeViewModelFactory(
            GpsLocation(requireContext()),
            requireContext(),
            Repository.getInstance(
                WeatherClient.getInstance(),
                LocalRepository.getInstance(requireContext())
            )
        )
        viewModel = ViewModelProvider(requireActivity(), factory).get(HomeViewModel::class.java)


        requestMyCurrentWeather()







        lifecycleScope.launch {
            viewModel.finalWeather.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                        //loading.show()
                    }
                    is ApiState.Success -> {

                        binding.permissionCard.visibility = View.GONE
                        Log.i("test", "call view model")
                        loading.dismiss()
                        binding.areaTxt.visibility = View.VISIBLE

                        try {
                            val geocoder =
                                Geocoder(requireContext(), Locale.forLanguageTag(language))
                            var addressList: List<Address> = geocoder.getFromLocation(
                                it.data.lat,
                                it.data.lon,
                                1
                            ) as List<Address>
                            if (addressList.size != 0) {
                                var area = addressList.get(0).countryName
                                var country = addressList.get(0).adminArea
                                binding.areaTxt.text = country + " , " + area
                            } else {
                                binding.areaTxt.text = it.data.timezone
                            }
                        } catch (e: IOException) {
                            binding.areaTxt.text = it.data.timezone
                        } catch (e: RemoteException) {
                            binding.areaTxt.text = it.data.timezone
                        }


                        var simpleDate = SimpleDateFormat(
                            "dd/M/yyyy - hh:mm:a ",
                            Locale.forLanguageTag(language)
                        )
                        var currentDate = simpleDate.format(it.data.current.dt.times(1000L))
                        binding.dateTxt.visibility = View.VISIBLE
                        binding.dateTxt.text = currentDate.toString()


                        binding.cardView.visibility = View.VISIBLE
                        binding.weatherTempTxt.visibility = View.VISIBLE
                        binding.weatherStatusTxt.visibility = View.VISIBLE
                        binding.weatherIconImg.visibility = View.VISIBLE
                        binding.weatherStatusTxt.text = it.data.current.weather.get(0).description
                        var temp = it.data.current.temp
                        var intTemp = Math.ceil(temp).toInt()

                        var finalTemp = ""
                        if (language.equals("en")) {
                            finalTemp =
                                if (units.equals("standard")) "$intTemp°K" else if (units.equals("metric")) "$intTemp°C" else "$intTemp°F"
                        } else {
                            finalTemp =
                                if (units.equals("standard")) "$intTemp°ك " else if (units.equals("metric")) "$intTemp°س " else "$intTemp°ف "
                        }


                        binding.weatherTempTxt.text = finalTemp



                        binding.weatherIconImg.setImageResource(
                            MyWeatherIcons.mapIcon[it.data.current.weather.get(
                                0
                            ).icon]!!
                        )


                        binding.detailsCard.visibility = View.VISIBLE
                        binding.pressureTxt.text = it.data.current.pressure.toString()
                        binding.humidityTxt.text = it.data.current.humidity.toString()
                        binding.windTxt.text = it.data.current.wind_speed.toString()
                        binding.cloudTxt.text = it.data.current.clouds.toString()
                        binding.ultraVioletTxt.text = it.data.current.uvi.toString()
                        binding.visibilityTxt.text = it.data.current.visibility.toString()

                        binding.weeklyWeatherRv.visibility = View.VISIBLE
                        binding.hourlyWeatherRv.visibility = View.VISIBLE
                        var manger = LinearLayoutManager(requireContext())
                        manger.orientation = RecyclerView.HORIZONTAL
                        binding.hourlyWeatherRv.layoutManager = manger
                        binding.hourlyWeatherRv.adapter =
                            DayAdapter(requireContext(), it.data.hourly)
                        var mangerVertical = LinearLayoutManager(requireContext())
                        mangerVertical.orientation = RecyclerView.VERTICAL
                        binding.weeklyWeatherRv.layoutManager = mangerVertical
                        binding.weeklyWeatherRv.adapter =
                            WeekAdapter(requireContext(), it.data.daily)

                    }
                    else -> {
                        loading.dismiss()
                        layout = binding.homeConstrain
                        val snackbar = Snackbar.make(
                            layout,
                            getString(R.string.api_error),
                            Snackbar.ANIMATION_MODE_SLIDE
                        )
                        snackbar.view.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.settingselectors)
                        snackbar.show()

                    }

                }

            }

        }





        binding.swipeRefreshLayout.setOnRefreshListener {
            requestMyCurrentWeather()
        }
        binding.allow.setOnClickListener {
            requestPermission()
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

    fun requestMyCurrentWeather() {
        if (NetworkConnection.getConnectivity(requireContext())) {
            binding.swipeRefreshLayout.isRefreshing = false

            when (status) {
                "map" -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.choose_location),
                        Toast.LENGTH_LONG
                    ).show()
                    var action = HomeFragmentDirections.actionHomeFragmentToMapsFragment("home")
                    Navigation.findNavController(requireView()).navigate(action)
                }
                "gps" -> {
                    checkPermissionStatus()

                }
                "mapResult" -> {
                    loading.show()
                    var latitude = sharedPref.getFloat("lat", 0f).toDouble()
                    var longitude = sharedPref.getFloat("lon", 0f).toDouble()
                    viewModel.getMyWeatherStatus(latitude, longitude, language, units)


                }
            }
        } else {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.getMyBackupLocation()
            layout = binding.homeConstrain
            val snackbar = Snackbar.make(
                layout,
                getString(R.string.no_internet),
                Snackbar.ANIMATION_MODE_SLIDE
            )
            snackbar.view.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.settingselectors)
            snackbar.show()

        }
    }


    fun excudeGpsCall() {
        loading.show()
        viewModel.getMyGpsLocation(language, units)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == My_LOCATION_PERMISSION_ID) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                binding.permissionCard.visibility = View.GONE
                excudeGpsCall()

            } else {

                binding.permissionCard.visibility = View.VISIBLE

            }
        }

    }


    @SuppressLint("MissingPermission")
    fun checkPermissionStatus() {
        if (checkPermission()) {

            if (isLocationEnabled()) {
                excudeGpsCall()
            } else {
                Toast.makeText(requireContext(), "please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                requireContext().startActivity(intent)
            }

        } else {
            if (!requestPermissionStatus) {
                requestPermissionStatus = true
                requestPermission()
            }

        }

    }

    private fun checkPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED)
                ||
                (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                        PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), My_LOCATION_PERMISSION_ID
        )
    }


    private fun isLocationEnabled(): Boolean {
        val locationManger: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManger.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


}