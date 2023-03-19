package com.example.weatherforecast.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentViewPagerBinding
import com.example.weatherforecast.onboarding.screens.FirstScreen
import com.example.weatherforecast.onboarding.screens.SecondScreen
import com.example.weatherforecast.onboarding.screens.ThirdScreen





class ViewPagerFragment : Fragment() {
    lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_view_pager, container, false)
        binding.lifecycleOwner
        val fragmentList = arrayListOf<Fragment>(
          FirstScreen(),SecondScreen(),ThirdScreen()
        )

        val adapter = ViewPagerAdapter(fragmentList,requireActivity().supportFragmentManager,lifecycle)
        binding.viewPager.adapter=adapter
        return  binding.root
    }

}