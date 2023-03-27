package com.example.weatherforecast.onboarding.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentThirdBinding


class ThirdScreen : Fragment() {
    lateinit var binding:FragmentThirdBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_third, container, false)
        binding.lifecycleOwner
        val viewPager =activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.finish.setOnClickListener{
            if(binding.IntialradioGroup.checkedRadioButtonId ==-1){
                Toast.makeText(requireContext(),"Please choose one of location options",Toast.LENGTH_LONG).show()
            }else{
                onBoardingFinished()
                findNavController().navigate(R.id.action_viewPagerFragment_to_homeFragment)
            }
        }
        return binding.root
    }

    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished",true)
        var selectedItem = binding.IntialradioGroup.checkedRadioButtonId
        if(selectedItem == R.id.radio_map){
            editor.putString("location","map")
        }else{
            editor.putString("location","gps")
        }
        editor.apply()
    }
}
