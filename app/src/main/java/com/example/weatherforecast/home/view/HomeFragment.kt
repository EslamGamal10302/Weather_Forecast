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
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.home.viewModel.HomeViewModel
import com.example.weatherforecast.home.viewModel.HomeViewModelFactory
import com.example.weatherforecast.network.WeatherClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            binding.weatherStatusTxt.text=it.current.weather.get(0).description
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
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        binding.lifecycleOwner
        return binding.root
    }


}