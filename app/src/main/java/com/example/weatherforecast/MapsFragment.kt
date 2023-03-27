package com.example.weatherforecast

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.weatherforecast.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapsFragment : Fragment() {
    lateinit var binding: FragmentMapsBinding
    lateinit var map : GoogleMap
    var lat :Double = 0.0
    var lon :Double = 0.0
    lateinit var location : LatLng
    val args : MapsFragmentArgs by navArgs()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        searchOnMap()
        googleMap.clear()
        googleMap.setOnMapClickListener { location->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(location))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            lat = location.latitude
            lon = location.latitude
            Log.i("test","$lat $lon")

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       binding=FragmentMapsBinding.inflate(inflater,container,false)
        binding.add.setOnClickListener {
            when(args.status){
                "home" -> {
                    val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("location","mapResult")
                    editor.putFloat("lat",lat.toFloat())
                    editor.putFloat("lon",lon.toFloat())
                    editor.apply()
                var action = MapsFragmentDirections.actionMapsFragmentToHomeFragment()
                    Navigation.findNavController(requireView()).navigate(action)

                }
                "fav"->{ var action = MapsFragmentDirections.actionMapsFragmentToFavoritesFragment(LatLng(lat,lon))
                    Navigation.findNavController(it).navigate(action)}
            }
        }
        return binding.root
    //inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    private fun searchOnMap(){
        binding.search.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH||
                actionId== EditorInfo.IME_ACTION_DONE||
                event.action == KeyEvent.ACTION_DOWN||
                event.action == KeyEvent.KEYCODE_ENTER
            ){
                goToSearchLocation()
                true
            } else {
                binding.search.text.clear()
                binding.search.hint = "Please Enter Your Location"
                false
            }
        }
    }

    private fun goToSearchLocation() {
        var searchLocation = binding.search.text.toString()
        var geoCoder: Geocoder = Geocoder(requireContext())
        var list :List<Address> = listOf()
        try {
            list = geoCoder.getFromLocationName(searchLocation , 1) as List<Address>
        }catch(e: IOException){
            e.printStackTrace()
        }
        if (list.size >0) {
            var adress: Address = list.get(0)
            var location: String = adress.adminArea
            var y = adress.featureName
            var x = adress.countryName
            lat  = adress.latitude
            lon = adress.longitude
            Log.i("test","$lat $lon $y,$x")
            goToLatAndLonf(lat , lon , 17f)
        }

    }

    private fun goToLatAndLonf(latitude: Double, longitude: Double, float: Float) {
        var latLng = LatLng(latitude , longitude)
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng ,float)
        map.addMarker(MarkerOptions().position(latLng))
        map.animateCamera(cameraUpdate)
    }
}