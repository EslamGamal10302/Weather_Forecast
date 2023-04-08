package com.example.weatherforecast.favorites.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.NetworkConnection
import com.example.weatherforecast.R
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.databinding.FragmentFavoritesBinding
import com.example.weatherforecast.favorites.viewModel.FavoritesViewModel
import com.example.weatherforecast.favorites.viewModel.FavoritesViewModelFactory
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.home.view.DayAdapter
import com.example.weatherforecast.network.WeatherClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoritesFragment : Fragment(), FavoriteOnClickListner {
    lateinit var binding: FragmentFavoritesBinding
    val args: FavoritesFragmentArgs by navArgs()
    lateinit var factory: FavoritesViewModelFactory
    lateinit var viewModel: FavoritesViewModel
    lateinit var layout: ConstraintLayout
    lateinit var snackbar: Snackbar

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.favorites)
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        binding.lifecycleOwner
        factory = FavoritesViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                LocalRepository.getInstance(requireContext())
            )
        )
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(FavoritesViewModel::class.java)
        if (args.location != null) {
            Log.i("result", "${args.location!!.latitude}  ${args.location!!.longitude}")
            viewModel.addToFavorites(
                MyLocations(
                    args.location!!.latitude,
                    args.location!!.longitude
                )
            )

        } else {
            viewModel.getAllFavLocations()

        }


        lifecycleScope.launch {
            viewModel.finalWeather.collectLatest{
                Log.i("data", it.toString())
                var manger = LinearLayoutManager(requireContext())
                manger.orientation = RecyclerView.VERTICAL
                binding.favRv.layoutManager = manger
                if(it.size>0){
                    binding.splashLottie.visibility=View.GONE
                    binding.message.visibility=View.GONE
                } else{
                    binding.splashLottie.visibility=View.VISIBLE
                    binding.message.visibility=View.VISIBLE
                    binding.splashLottie.animate().setDuration(10000).setStartDelay(1500);
                }
                binding.favRv.adapter = FavoriteAdapter(requireContext(), it, this@FavoritesFragment)

            }
        }


        binding.floatingActionButton3.setOnClickListener {
            // Navigation.findNavController(it).navigate(R.id.action_favoritesFragment_to_mapsFragment)

            if (NetworkConnection.getConnectivity(requireContext())) {
                var action =
                    FavoritesFragmentDirections.actionFavoritesFragmentToMapsFragment("fav")
                Navigation.findNavController(requireView()).navigate(action)
            } else {
                snackbar.show()
            }

        }
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout = binding.favConstrain
        snackbar =
            Snackbar.make(layout, getString(R.string.no_internet), Snackbar.ANIMATION_MODE_SLIDE)
       // snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.medium_purple))
        snackbar.view.background=ContextCompat.getDrawable(requireContext(),R.drawable.settingselector2)
    }

    override fun removeFromFavorite(data: MyLocations) {
        viewModel.deleteFromFavorites(data)
    }

    override fun showFavoriteLocationWeather(latitude: Double, longitude: Double) {
        if (NetworkConnection.getConnectivity(requireContext())) {
            var action = FavoritesFragmentDirections.actionFavoritesFragmentToFavoriteWeather2(
                LatLng(
                    latitude,
                    longitude
                )
            )
            Navigation.findNavController(requireView()).navigate(action)
        } else {
           snackbar.show()
        }
    }


}