package com.example.weatherforecast.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentFirstScreenBinding


class FirstScreen : Fragment() {
    lateinit var binding: FragmentFirstScreenBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_first_screen, container, false)
        binding.lifecycleOwner
        val viewPager =activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.next.setOnClickListener{
            viewPager?.currentItem=1
        }
        return binding.root
    }


}