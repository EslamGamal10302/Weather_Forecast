package com.example.weatherforecast.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.Hourly
import com.example.weatherforecast.MyWeatherIcons
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.DayWeatherBinding
import java.text.SimpleDateFormat
import java.util.*

class DayAdapter(var context: Context,var myDayWeather:List<Hourly>):RecyclerView.Adapter<DayAdapter.DayViewHolder>() {
    lateinit var binding: DayWeatherBinding
    val sharedPref = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val units = sharedPref.getString("units","metric")
    class DayViewHolder(var binding: DayWeatherBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding=DataBindingUtil.inflate(inflater, R.layout.day_weather,parent,false)
        return DayViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myDayWeather.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val weather=myDayWeather[position]
        var date=Date(weather.dt*1000L)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone=TimeZone.getDefault()
        var formatedData=sdf.format(date)
       holder.binding.daillyDayTxt.text=formatedData

        var temp=weather.temp
        var intTemp = Math.ceil(temp).toInt()
        var finalTemp = if(units.equals("standard")) "$intTemp°K" else if (units.equals("metric")) "$intTemp°C" else "$intTemp°F"
       // var tempCelucis= "$intTemp°C"
        holder.binding.daillyTempTxt.text=finalTemp

       /* val url = "https://openweathermap.org/img/wn/${weather.weather.get(0).icon}@2x.png"
        Glide.with(context).load(url).into(holder.binding.dailyWeatherImg) */
        holder.binding.dailyWeatherImg.setImageResource(MyWeatherIcons.mapIcon[weather.weather.get(0).icon]!!)

    }
}