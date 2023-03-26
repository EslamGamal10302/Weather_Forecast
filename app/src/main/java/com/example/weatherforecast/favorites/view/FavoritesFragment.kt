package com.example.weatherforecast.favorites.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentFavoritesBinding


class FavoritesFragment : Fragment() {
   lateinit var binding: FragmentFavoritesBinding

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.title="Favorites"
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        binding.lifecycleOwner
        val args : FavoritesFragmentArgs by navArgs()
        if(args.location!=null){
            Log.i("result","${args.location!!.latitude}  ${ args.location!!.longitude}")
        }
        binding.floatingActionButton3.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_favoritesFragment_to_mapsFragment)
        }
        return binding.root
    }


}