package com.example.weatherforecast.alerts.view

import android.annotation.SuppressLint
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.R
import com.example.weatherforecast.alerts.viewModel.*
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.databinding.FragmentAlertsBinding
import com.example.weatherforecast.favorites.view.FavoriteAdapter
import com.example.weatherforecast.favorites.viewModel.FavoritesViewModel
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.network.WeatherClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit


class AlertsFragment : Fragment(), AlertOnClickListner {
    lateinit var binding: FragmentAlertsBinding
    var alarmManager: AlarmManager? = null
    lateinit var pending: PendingIntent
    lateinit var alertDialog: DialogAlertFragment
    lateinit var factory: AlertsViewModelFactory
    lateinit var viewModel: AlertsViewModel
    var requestCode = 0
    var days: Long = 0

    // var interval:Long = 24*60*60*1000
    var interval: Long = 1 * 3 * 60 * 1000


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.alerts)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alerts, container, false)
        binding.lifecycleOwner
        factory = AlertsViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                LocalRepository.getInstance(requireContext())
            )
        )
        viewModel = ViewModelProvider(requireActivity(), factory).get(AlertsViewModel::class.java)
        createNotificationChannel()



        binding.floatingAlarmActionButton.setOnClickListener {
            alertDialog = DialogAlertFragment(this)
            activity?.supportFragmentManager?.let { manger -> alertDialog.show(manger, "dialog") }
        }


        lifecycleScope.launch {
            viewModel.finalAlerts.collectLatest {
                var manger = LinearLayoutManager(requireContext())
                manger.orientation = RecyclerView.VERTICAL
                binding.alertRv.layoutManager = manger
                if (it.size > 0) {
                    binding.alrtSplashLottie.visibility = View.GONE
                    binding.messageAlert.visibility = View.GONE
                } else {
                    binding.alrtSplashLottie.visibility = View.VISIBLE
                    binding.messageAlert.visibility = View.VISIBLE
                    binding.alrtSplashLottie.animate().setDuration(10000).setStartDelay(1500);
                }
                binding.alertRv.adapter = AlertAdapter(requireContext(), this@AlertsFragment, it)
            }
        }


        return binding.root
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MyFirstChannel"
            val description = "Channel for notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("myChannel", name, importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun setAlarm(data: MyUserAlert) {
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(), AlarmReciever::class.java)
        val numberOfDaysInMillis = (data.dateTo) - (data.dateFrom)
        days = TimeUnit.MILLISECONDS.toDays(numberOfDaysInMillis)




        for (i in 0..days) {
            requestCode = data.id + (i.toInt())
            intent.putExtra("alert", requestCode)
            intent.putExtra("type", data.type)
            if (i == days) {
                intent.putExtra("id", data.id)
            }
            pending =
                getBroadcast(requireContext(), requestCode, intent, PendingIntent.FLAG_MUTABLE)
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

            alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, trigerTime + (i * interval), pending);
            if (data.type.equals("Notification")) {
                deleteCompletedNotification(data, i, trigerTime + (i * interval))
            } else {
                deleteCompletedAlarmNotification(data, i, trigerTime + (i * interval))
            }


        }


    }

    private fun deleteCompletedNotification(data: MyUserAlert, i: Long, trigerTime: Long) {
        var hoursDelay = data.timeTo - data.timeFrom
        var finalTrigger = hoursDelay + trigerTime
        val delteIntent = Intent(requireActivity(), DeleteAlarmReciever::class.java)
        delteIntent.putExtra("alert", data.id + i.toInt())
        val deletePending = getBroadcast(
            requireContext(),
            data.id + i.toInt(),
            delteIntent,
            PendingIntent.FLAG_MUTABLE
        )

        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, finalTrigger, deletePending);
    }

    private fun deleteCompletedAlarmNotification(data: MyUserAlert, i: Long, trigerTime: Long) {
        var hoursDelay = data.timeTo - data.timeFrom
        var finalTrigger = hoursDelay + trigerTime
        val delteIntent = Intent(requireActivity(), DeleteAlarmNotificationReciever::class.java)
        delteIntent.putExtra("alert", data.id + i.toInt())
        val deletePending = getBroadcast(
            requireContext(),
            data.id + i.toInt(),
            delteIntent,
            PendingIntent.FLAG_MUTABLE
        )
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, finalTrigger, deletePending);
    }

    override fun onDialogSave(data: MyUserAlert) {

        viewModel.addAlert(data)
        setAlarm(data)

    }

    override fun deleteAlert(data: MyUserAlert) {
        viewModel.deleteAlert(data)
        cancelAlarm(data)
    }

    @SuppressLint("SuspiciousIndentation")
    fun cancelAlarm(data: MyUserAlert) {
        val numberOfDaysInMillisForDelete = (data.dateTo) - (data.dateFrom)
        val daysToDelete = TimeUnit.MILLISECONDS.toDays(numberOfDaysInMillisForDelete)
        if (alarmManager == null) {
            alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        }

        val intent = Intent(requireActivity(), AlarmReciever::class.java)
        for (i in 0..daysToDelete) {
            pending = getBroadcast(
                requireContext(),
                data.id + (i.toInt()),
                intent,
                PendingIntent.FLAG_MUTABLE
            )
            alarmManager!!.cancel(pending)
        }
    }

}