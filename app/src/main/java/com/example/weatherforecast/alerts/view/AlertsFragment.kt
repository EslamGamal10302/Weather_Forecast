package com.example.weatherforecast.alerts.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getBroadcast
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weatherforecast.R
import com.example.weatherforecast.alerts.viewModel.AlarmReciever
import com.example.weatherforecast.databinding.FragmentAlertsBinding


class AlertsFragment : Fragment() {
    lateinit var binding: FragmentAlertsBinding
    lateinit var alarmManager : AlarmManager
    lateinit var pending : PendingIntent
    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.title=requireActivity().getString(R.string.alerts)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_alerts, container, false)
        binding.lifecycleOwner
        createNotificationChannel()
        setAlarm()
        return binding.root
    }

   fun createNotificationChannel(){
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           val name : CharSequence = "MyFirstChannel"
           val description = "Channel for notification"
           val importance =NotificationManager.IMPORTANCE_HIGH
           val channel = NotificationChannel("myChannel",name,importance)
           channel.description=description
           val notificationManager = activity?.getSystemService(NotificationManager::class.java)
           notificationManager?.createNotificationChannel(channel)
       }
   }
    fun setAlarm(){
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent  = Intent(requireActivity(),AlarmReciever::class.java)
        pending = getBroadcast(requireContext(),0,intent,PendingIntent.FLAG_MUTABLE)
      //  val triggerTime = System.currentTimeMillis() + 60000 // 60 seconds from now
        val triggerTime = 1680652920000
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pending);


    }


}