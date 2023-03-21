package com.example.weatherforecast.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Daily
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.WeekWeatherBinding

class WeekAdapter(var context: Context,var myWeekWeather:List<Daily>):RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {
    lateinit var binding: WeekWeatherBinding
    class WeekViewHolder(var binding: WeekWeatherBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= DataBindingUtil.inflate(inflater, R.layout.week_weather,parent,false)
        return WeekViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myWeekWeather.size
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val weather = myWeekWeather[position]

    }
}