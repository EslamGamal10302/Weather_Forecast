package com.example.weatherforecast.favoriteWeather.view

import android.app.ProgressDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.ApiState
import com.example.weatherforecast.R
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.databinding.FragmentFavoriteWeatherBinding
import com.example.weatherforecast.favoriteWeather.viewModel.FavoriteWeatherViewModel
import com.example.weatherforecast.favoriteWeather.viewModel.FavoriteWeatherViewModelFactory
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.home.view.DayAdapter
import com.example.weatherforecast.home.view.WeekAdapter
import com.example.weatherforecast.network.WeatherClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class FavoriteWeather : Fragment() {
    lateinit var binding: FragmentFavoriteWeatherBinding
    val args :FavoriteWeatherArgs by navArgs()
    lateinit var factory : FavoriteWeatherViewModelFactory
    lateinit var viewModel : FavoriteWeatherViewModel
    lateinit var loading: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_weather, container, false)
        binding.lifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("yarb","${args.location!!.latitude}  ${ args.location!!.longitude}")
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val units = sharedPref.getString("units","metric")
        val language = sharedPref.getString("language","en")
        factory= FavoriteWeatherViewModelFactory( Repository.getInstance(
            WeatherClient.getInstance(),
            LocalRepository.getInstance(requireContext())))
        viewModel= ViewModelProvider(requireActivity(),factory).get(FavoriteWeatherViewModel::class.java)
        viewModel.getMyWeatherStatus(args.location!!.latitude,args.location!!.longitude,language!!,units!!)



        lifecycleScope.launch {

            viewModel.finalWeather.collectLatest {

                when (it){
                    is ApiState.Loading ->{
                        loading.show()
                    }
                    is ApiState.Success ->{
                        loading.dismiss()
                        binding.areaTxt.visibility = View.VISIBLE
                        try{
                            val geocoder = Geocoder(requireContext(), Locale.getDefault())
                            var addressList:List<Address> = geocoder.getFromLocation(it.data.lat,it.data.lon,1) as List<Address>
                            if (addressList.size != 0){
                                var area = addressList.get(0).countryName
                                var country = addressList.get(0).adminArea
                                binding.areaTxt.text = country +" , "+ area
                            }else{
                                binding.areaTxt.text =it.data.timezone
                            }
                        }catch (e : IOException){
                            binding.areaTxt.text = it.data.timezone
                        }catch (e: RemoteException){
                            binding.areaTxt.text = it.data.timezone
                        }

                        var simpleDate = SimpleDateFormat("dd/M/yyyy - hh:mm:a ")
                        var currentDate = simpleDate.format(it.data.current.dt.times(1000L))
                        binding.dateTxt.visibility = View.VISIBLE
                        binding.dateTxt.text = currentDate.toString()


                        binding.myFavcardView.visibility = View.VISIBLE
                        binding.weatherTempTxt.visibility = View.VISIBLE
                        binding.weatherStatusTxt.visibility = View.VISIBLE
                        binding.myFavWeatherIconImg.visibility = View.VISIBLE
                        binding.weatherStatusTxt.text = it.data.current.weather.get(0).description
                        var temp = it.data.current.temp
                        var intTemp = Math.ceil(temp).toInt()
                        // var tempCelucis = "$intTemp°C"
                        var finalTemp =
                            if (units.equals("standard")) "$intTemp°K" else if (units.equals("metric")) "$intTemp°C" else "$intTemp°F"
                        binding.weatherTempTxt.text = finalTemp
                        val url =
                            "https://openweathermap.org/img/wn/${it.data.current.weather.get(0).icon}@2x.png"
                        Glide.with(requireContext()).load(url).into(binding.myFavWeatherIconImg)

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
                        binding.hourlyWeatherRv.adapter = DayAdapter(requireContext(), it.data.hourly)
                        var mangerVertical = LinearLayoutManager(requireContext())
                        mangerVertical.orientation = RecyclerView.VERTICAL
                        binding.weeklyWeatherRv.layoutManager = mangerVertical
                        binding.weeklyWeatherRv.adapter = WeekAdapter(requireContext(), it.data.daily)

                    }
                    else ->{
                        loading.dismiss()
                        var layout= binding.favWeatherConst
                        val snackbar= Snackbar.make(layout,getString(R.string.api_error), Snackbar.ANIMATION_MODE_SLIDE)
                        snackbar.view.background= ContextCompat.getDrawable(requireContext(),R.drawable.settingselectors)
                        snackbar.show()
                    }

                }


            }

        }



    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loading = ProgressDialog(context)
        loading.setMessage(getString(R.string.loading))
        loading.show()
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.title=requireActivity().getString(R.string.favorites)
    }
}