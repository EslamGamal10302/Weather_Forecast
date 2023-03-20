package com.example.weatherforecast.favorites.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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
        return binding.root
    }


}