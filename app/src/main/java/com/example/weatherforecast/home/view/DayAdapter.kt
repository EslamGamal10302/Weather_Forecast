package com.example.weatherforecast.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.Hourly
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.DayWeatherBinding

class DayAdapter(var context: Context,var myDayWeather:List<Hourly>):RecyclerView.Adapter<DayAdapter.DayViewHolder>() {
    lateinit var binding: DayWeatherBinding
    class DayViewHolder(var binding: DayWeatherBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding=DataBindingUtil.inflate(inflater, R.layout.day_weather,parent,false)
        return DayViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 24
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val weather=myDayWeather[position]
        binding.daillyDayTxt.text=weather.dt.toString()
        binding.daillyTempTxt.text=weather.temp.toString()

    }
}