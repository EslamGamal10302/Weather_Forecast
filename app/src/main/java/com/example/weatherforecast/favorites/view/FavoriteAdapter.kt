package com.example.weatherforecast.favorites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Hourly
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.DayWeatherBinding
import com.example.weatherforecast.databinding.FavLocationsBinding
import com.example.weatherforecast.home.view.DayAdapter

class FavoriteAdapter (var context: Context, var myFavLocations:List<MyLocations>): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    lateinit var binding: FavLocationsBinding
    class FavoriteViewHolder(var binding: FavLocationsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= DataBindingUtil.inflate(inflater, R.layout.fav_locations,parent,false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myFavLocations.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        var myFavLocation=myFavLocations[position]
        holder.binding.weeklyWeatherStatusTxt.text="${myFavLocation.latitude}  ${myFavLocation.longitude}"
    }


}