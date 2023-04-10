package com.example.weatherforecast.settings.view

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.weatherforecast.NetworkConnection
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var snackbar: Snackbar
    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.settings)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val status = sharedPref.getString("location", "")
        val units = sharedPref.getString("units", "metric")
        val language = sharedPref.getString("language", "en")
        val notificatioStatus = sharedPref.getString("notification_status", "on")
        val editor = sharedPref.edit()
        val layout = binding.settingsConstrain
        snackbar =
            Snackbar.make(layout, getString(R.string.no_internet), Snackbar.ANIMATION_MODE_SLIDE)
        snackbar.view.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.settingselector2)

        if (status.equals("mapResult")) {
            binding.radioMap.isChecked = true
        } else {
            binding.radioGps.isChecked = true
        }


        if (units.equals("standard")) {
            binding.radioStandard.isChecked = true
        } else if (units.equals("imperial")) {
            binding.radioImperial.isChecked = true
        } else {
            binding.radioMetric.isChecked = true
        }

        if (language.equals("ar")) {
            binding.radioArabic.isChecked = true
        } else {
            binding.radioEnglish.isChecked = true
        }

        if (notificatioStatus.equals("on")) {
            binding.toggleSwitch.isChecked = true
        } else {
            binding.toggleSwitch.isChecked = false
        }




        binding.radioMap.setOnClickListener {
            if (NetworkConnection.getConnectivity(requireContext())) {
                editor.putString("location", "map")
                editor.apply()
            } else {
                snackbar.show()
            }

        }
        binding.radioGps.setOnClickListener {
            if (NetworkConnection.getConnectivity(requireContext())) {
                editor.putString("location", "gps")
                editor.apply()
            } else {
                snackbar.show()
            }
        }
        binding.radioArabic.setOnClickListener {
            editor.putString("language", "ar")
            editor.apply()

            changeLanguage("ar")

        }
        binding.radioEnglish.setOnClickListener {
            editor.putString("language", "en")
            editor.apply()

            changeLanguage("en")

        }
        binding.radioStandard.setOnClickListener {
            if (NetworkConnection.getConnectivity(requireContext())) {
                editor.putString("units", "standard")
                editor.apply()
            } else {
                snackbar.show()
            }
        }
        binding.radioMetric.setOnClickListener {
            if (NetworkConnection.getConnectivity(requireContext())) {
                editor.putString("units", "metric")
                editor.apply()
            } else {
                snackbar.show()
            }
        }
        binding.radioImperial.setOnClickListener {
            if (NetworkConnection.getConnectivity(requireContext())) {
                editor.putString("units", "imperial")
                editor.apply()
            } else {
                snackbar.show()
            }
        }
        binding.toggleSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor.putString("notification_status", "on")
                editor.apply()
                Log.i("noti", sharedPref.getString("notification_status", "on").toString())

            } else {
                editor.putString("notification_status", "off")
                editor.apply()
                Log.i("noti", sharedPref.getString("notification_status", "on").toString())

            }
        }

    }

    fun changeLanguage(lang: String) {
        var local = Locale(lang)
        var config: Configuration = resources.configuration
        config.setLocale(local)
        //var appContext = activity?.applicationContext
        var appResources = requireContext()?.resources
        appResources?.updateConfiguration(config, appResources.displayMetrics)
        var intent = activity?.intent
        activity?.finish()
        startActivity(intent)
    }


}







