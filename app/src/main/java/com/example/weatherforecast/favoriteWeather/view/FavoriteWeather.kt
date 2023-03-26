package com.example.weatherforecast.favoriteWeather.view

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.databinding.FragmentFavoriteWeatherBinding
import com.example.weatherforecast.favoriteWeather.viewModel.FavoriteWeatherViewModel
import com.example.weatherforecast.favoriteWeather.viewModel.FavoriteWeatherViewModelFactory
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.home.view.DayAdapter
import com.example.weatherforecast.home.view.WeekAdapter
import com.example.weatherforecast.network.WeatherClient
import java.text.SimpleDateFormat


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
        factory= FavoriteWeatherViewModelFactory(  Repository.getInstance(
            WeatherClient.getInstance(),
            LocalRepository.getInstance(requireContext())))
        viewModel= ViewModelProvider(requireActivity(),factory).get(FavoriteWeatherViewModel::class.java)
        viewModel.getMyWeatherStatus(args.location!!.latitude,args.location!!.longitude)
        viewModel.finalWeather.observe(viewLifecycleOwner){
            loading.dismiss()
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
            var tempCelucis = "$intTemp°C"
            binding.weatherTempTxt.text = tempCelucis
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loading = ProgressDialog(context)
        loading.setMessage("Loading.....")
        loading.show()
    }
}