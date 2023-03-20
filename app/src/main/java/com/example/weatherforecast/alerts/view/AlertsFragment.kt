package com.example.weatherforecast.alerts.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentAlertsBinding


class AlertsFragment : Fragment() {
    lateinit var binding: FragmentAlertsBinding
    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.title="Alerts"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_alerts, container, false)
        binding.lifecycleOwner
        return binding.root
    }


}