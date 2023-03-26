package com.example.weatherforecast.favorites.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.R
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.databinding.FragmentFavoritesBinding
import com.example.weatherforecast.favorites.viewModel.FavoritesViewModel
import com.example.weatherforecast.favorites.viewModel.FavoritesViewModelFactory
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.home.view.DayAdapter
import com.example.weatherforecast.network.WeatherClient


class FavoritesFragment : Fragment() {
   lateinit var binding: FragmentFavoritesBinding
    val args : FavoritesFragmentArgs by navArgs()
    lateinit var factory:FavoritesViewModelFactory
    lateinit var viewModel:FavoritesViewModel

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.title="Favorites"
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        binding.lifecycleOwner
        factory= FavoritesViewModelFactory(Repository.getInstance(WeatherClient.getInstance(),LocalRepository.getInstance(requireContext())))
        viewModel=ViewModelProvider(requireActivity(),factory).get(FavoritesViewModel::class.java)
        if(args.location!=null){
            Log.i("result","${args.location!!.latitude}  ${ args.location!!.longitude}")
            viewModel.addToFavorites(MyLocations(args.location!!.latitude,args.location!!.longitude))

        } else{
            viewModel.getAllFavLocations()

        }
        viewModel.finalWeather.observe(viewLifecycleOwner){
            Log.i("data",it.toString())
            var manger = LinearLayoutManager(requireContext())
            manger.orientation = RecyclerView.VERTICAL
            binding.favRv.layoutManager = manger
            binding.favRv.adapter = FavoriteAdapter(requireContext(), it)
        }

        binding.floatingActionButton3.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_favoritesFragment_to_mapsFragment)
        }
        return binding.root
    }


}