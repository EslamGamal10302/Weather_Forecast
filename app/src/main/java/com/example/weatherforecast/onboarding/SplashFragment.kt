package com.example.weatherforecast.onboarding

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSplashBinding
import java.util.*


class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        binding.lifecycleOwner
        binding.splashLottie.animate().setDuration(10000).setStartDelay(1500);
        Handler().postDelayed({
            if (onBoardingFinished()) {
                setLocale()
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                setLocale()
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }

        }, 4500)


        return binding.root
    }



    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    private fun setLocale() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        var lang = sharedPref.getString("language", "en").toString()
        var local = Locale(lang)
        var config : Configuration=resources.configuration
        config.setLocale(local)
        var appResources=requireContext()?.resources
        appResources?.updateConfiguration(config,appResources.displayMetrics)
    }
}