package com.example.weatherforecast.home.view

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.home.viewModel.HomeViewModel
import com.example.weatherforecast.home.viewModel.HomeViewModelFactory
import com.example.weatherforecast.network.WeatherClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var factory:HomeViewModelFactory
    lateinit var viewModel:HomeViewModel
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
        (activity as AppCompatActivity?)?.supportActionBar?.title="Home"
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      factory= HomeViewModelFactory(Repository.getInstance(WeatherClient.getInstance()))
      viewModel=ViewModelProvider(this,factory).get(HomeViewModel::class.java)
        viewModel.finalWeather.observe(viewLifecycleOwner){
           binding.areaTxt.text=it.timezone


            var simpleDate = SimpleDateFormat("dd/M/yyyy")
            var currentDate = simpleDate.format(it.current.dt*1000L)
           // var date: Date = simpleDate.parse(currentDate)
            binding.dateTxt.text=currentDate.toString()


           /* var date= Date(it.current.dt*1000L)
            var sdf= SimpleDateFormat("dd/mm/yy")
            sdf.timeZone= TimeZone.getDefault()
            var formatedData=sdf.format(date)
            binding.dateTxt.text=formatedData*/


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        binding.lifecycleOwner
        return binding.root
    }


}