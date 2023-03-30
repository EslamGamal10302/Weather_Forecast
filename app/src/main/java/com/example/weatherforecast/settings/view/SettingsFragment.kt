package com.example.weatherforecast.settings.view

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSettingsBinding
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.title=requireActivity().getString(R.string.settings)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val status = sharedPref.getString("location","")
        val units = sharedPref.getString("units","metric")
        val language = sharedPref.getString("language","en")
        val editor = sharedPref.edit()

        if(status.equals("mapResult")){
            binding.radioMap.isChecked=true
        }else{
            binding.radioGps.isChecked=true
        }


        if(units.equals("standard")){
            binding.radioStandard.isChecked=true
        }else if (units.equals("imperial")){
            binding.radioImperial.isChecked=true
        } else{
            binding.radioMetric.isChecked=true
        }

        if(language.equals("ar")){
            binding.radioArabic.isChecked=true
        } else{
            binding.radioEnglish.isChecked=true
        }


        binding.radioMap.setOnClickListener {
            editor.putString("location","map")
            editor.apply()
        }
        binding.radioGps.setOnClickListener {
            editor.putString("location","gps")
            editor.apply()
        }
        binding.radioArabic.setOnClickListener {
            editor.putString("language","ar")
            editor.apply()
            //changeLanguage("ar")
            setLocale("ar")
            activity?.recreate()
        }
        binding.radioEnglish.setOnClickListener {
            editor.putString("language","en")
            editor.apply()
            //changeLanguage("en")
            setLocale("en")
            activity?.recreate()
        }
        binding.radioStandard.setOnClickListener {
            editor.putString("units","standard")
            editor.apply()
        }
        binding.radioMetric.setOnClickListener {
            editor.putString("units","metric")
            editor.apply()
        }
        binding.radioImperial.setOnClickListener {
            editor.putString("units","imperial")
            editor.apply()
        }

    }

    fun changeLanguage(lang:String) {
        var local = Locale(lang)
        var config : Configuration=resources.configuration
        config.setLocale(local)
        var context = requireContext().createConfigurationContext(config)
        var resources = context.resources
        var appContext = activity?.applicationContext
        var appResources=appContext?.resources
        appResources?.updateConfiguration(config,appResources.displayMetrics)
        var intent = activity?.intent
        activity?.finish()
        startActivity(intent)
    }

    private fun setLocale(lang: String?) {
        var locale = Locale(lang)
        Locale.setDefault(locale)
        var config = Configuration()
        config.setLocale(locale)
        context?.resources?.updateConfiguration(config,context?.resources?.displayMetrics)
        var languageSettngs = activity?.getSharedPreferences("language",MODE_PRIVATE)?.edit()?.apply{
            putString("My Lang",lang)
            apply()
        }
    }

}








/*  var local = Locale("ar")
        var resources : Resources = requireActivity().resources
        var config : Configuration=resources.configuration
        config.setLocale(local)
        resources.updateConfiguration(config,resources.displayMetrics)
        Navigation.findNavController(requireView()).navigate(R.id.action_settingsFragment_to_homeFragment) */





/* val local = Locale("ar")
           Locale.setDefault(local)
           val resources : Resources= requireContext().resources
           val config = Configuration(resources.configuration)
           config.locale=local
           config.setLayoutDirection(local)
           resources.updateConfiguration(config,resources.displayMetrics) */