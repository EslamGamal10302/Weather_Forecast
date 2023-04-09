package com.example.weatherforecast.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.Daily
import com.example.weatherforecast.MyWeatherIcons
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.WeekWeatherBinding
import java.text.SimpleDateFormat
import java.util.*

class WeekAdapter(var context: Context,var myWeekWeather:List<Daily>):RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {
    lateinit var binding: WeekWeatherBinding
    val sharedPref = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val units = sharedPref.getString("units","metric")
    val language = sharedPref.getString("language", "en").toString()
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
        var date= Date(weather.dt*1000L)
        var sdf= SimpleDateFormat("d",Locale.forLanguageTag(language))
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        var intDay=formatedData.toInt()
        var calendar=Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        var format=SimpleDateFormat("EEEE",Locale.forLanguageTag(language))
        var day=format.format(calendar.time)
        holder.binding.daillyDayTxt.text=day

       /* val url = "https://openweathermap.org/img/wn/${weather.weather.get(0).icon}@2x.png"
        Glide.with(context).load(url).into(holder.binding.dailyWeatherImg)*/
        holder.binding.dailyWeatherImg.setImageResource(MyWeatherIcons.mapIcon[weather.weather.get(0).icon]!!)
        holder.binding.weeklyWeatherStatusTxt.text=weather.weather.get(0).description
        var max = Math.ceil(weather.temp.max).toInt()
        var min = Math.ceil(weather.temp.min).toInt()
        //"$max/$min°C"
        var finalTemp=""
        if(language.equals("en")) {
            finalTemp =
                if (units.equals("standard")) "$max/$min°K" else if (units.equals("metric")) "$max/$min°C" else "$max/$min°F"
        }else{
            finalTemp= if (units.equals("standard")) "$max/$min °ك " else if (units.equals("metric")) "$max/$min°س " else "$max/$min°ف "
        }

        holder.binding.daillyTempTxt.text=finalTemp
    }
}