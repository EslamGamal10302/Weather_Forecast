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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.R
import com.example.weatherforecast.alerts.viewModel.AlarmReciever
import com.example.weatherforecast.alerts.viewModel.AlertOnClickListner
import com.example.weatherforecast.alerts.viewModel.AlertsViewModel
import com.example.weatherforecast.alerts.viewModel.AlertsViewModelFactory
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.databinding.FragmentAlertsBinding
import com.example.weatherforecast.favorites.viewModel.FavoritesViewModel
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.network.WeatherClient
import java.util.Calendar
import java.util.concurrent.TimeUnit


class AlertsFragment : Fragment(),AlertOnClickListner {
    lateinit var binding: FragmentAlertsBinding
    lateinit var alarmManager : AlarmManager
    lateinit var pending : PendingIntent
    lateinit var alertDialog :DialogAlertFragment
    lateinit var factory: AlertsViewModelFactory
    lateinit var viewModel: AlertsViewModel
    var requestCode = 0
   // var interval:Long = 24*60*60*1000
   var interval:Long = 1*2*60*1000


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.title=requireActivity().getString(R.string.alerts)
        Log.i("lifecycle","onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.i("lifecycle","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("lifecycle","onStop")
    }

    override fun onResume() {
        super.onResume()
        Log.i("lifecycle","onCResume")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("lifecycle","onViewCreated")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_alerts, container, false)
        binding.lifecycleOwner
        factory= AlertsViewModelFactory( Repository.getInstance(
            WeatherClient.getInstance(),
            LocalRepository.getInstance(requireContext())
        ))
        viewModel=ViewModelProvider(requireActivity(), factory).get(AlertsViewModel::class.java)
        createNotificationChannel()
        //setAlarm()
        alertDialog = DialogAlertFragment(this)
        binding.floatingAlarmActionButton.setOnClickListener {
            activity?.supportFragmentManager?.let { manger->alertDialog.show(manger,"dialog") }
        }
        Log.i("lifecycle","onCreateView")

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
    fun setAlarm(data: MyUserAlert){
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent  = Intent(requireActivity(),AlarmReciever::class.java)
        val numberOfDaysInMillis=(data.dateTo)-(data.dateFrom)
        val days=TimeUnit.MILLISECONDS.toDays(numberOfDaysInMillis)




        //for loop
        for(i in 0..days) {
            requestCode=data.id+(i.toInt())
            pending = getBroadcast(requireContext(), requestCode, intent, PendingIntent.FLAG_MUTABLE)
            intent.putExtra("alert",requestCode)
            var from = Calendar.getInstance()
            var current = Calendar.getInstance()
            current.timeInMillis = data.timeFrom
            from.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY))
            from.set(Calendar.MINUTE, current.get(Calendar.MINUTE))
            current.timeInMillis = data.dateFrom
            from.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH))
            from.set(Calendar.MONTH, current.get(Calendar.MONTH))
            from.set(Calendar.YEAR, current.get(Calendar.YEAR))
            var trigerTime = from.timeInMillis
            //start time
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, trigerTime+(i*interval), pending);
        }


    }

    override fun onDialogSave(data: MyUserAlert) {
        //view model call to insert in data base
        viewModel.addAlert(data)
        setAlarm(data)
        //call to set alarm
    }

}