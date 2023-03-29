package com.example.weatherforecast.favorites.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FavLocationsBinding
import java.io.IOException
import java.util.*

class FavoriteAdapter (var context: Context, var myFavLocations:List<MyLocations>,var listner: FavoriteOnClickListner): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
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
        var geoCoder: Geocoder = Geocoder(context, Locale.getDefault())
        var list :List<Address> = listOf()
        try {
            list = geoCoder.getFromLocation(myFavLocation.latitude,myFavLocation.longitude , 1) as List<Address>
        }catch(e: IOException){
            e.printStackTrace()
        }
        if (list.size >0) {
            var adress: Address = list.get(0)
            var k = adress.subAdminArea
            var y = adress.adminArea
            var x = adress.countryName
            holder.binding.weeklyWeatherStatusTxt.text="$k ,$x"
        }
        holder.binding.delete.setOnClickListener {
          //  listner.removeFromFavorite(myFavLocation)
            val yes = "YES,I'M SURE"
            val no = "NO,GO BACK"
            val builder = AlertDialog.Builder(context)
            builder.setMessage("You will lose it from your favourite  locations list")
            builder.setTitle("Wait ! Are You Sure You Want To Delete This location ?")
            builder.setCancelable(false)
            builder.setPositiveButton(Html.fromHtml("<font color='#0f4cbd'>$yes</font>"),
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    listner.removeFromFavorite(myFavLocation)
                    Toast.makeText(
                        context,
                        "Removed from your favorite list",
                        Toast.LENGTH_SHORT
                    ).show()
                } as DialogInterface.OnClickListener)
            builder.setNegativeButton(Html.fromHtml("<font color='#0f4cbd'>$no</font>"),
                DialogInterface.OnClickListener { dialog: DialogInterface, which: Int -> dialog.cancel() } as DialogInterface.OnClickListener)

            val alertDialog = builder.create()

            alertDialog.show()

        }
        holder.binding.dailyConstrain.setOnClickListener {
            listner.showFavoriteLocationWeather(myFavLocation.latitude,myFavLocation.longitude)
        }
        }


    }

