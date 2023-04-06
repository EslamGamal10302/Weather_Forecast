package com.example.weatherforecast.alerts.viewModel

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherforecast.R
import com.example.weatherforecast.alerts.view.AlertsFragment
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.network.WeatherClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReciever: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val repo =   Repository.getInstance(
            WeatherClient.getInstance(),
            LocalRepository.getInstance(context!!)
        )
        CoroutineScope(Dispatchers.IO).launch{
          val response = repo.RS.getCurrentWeather(31.0,31.0,"en","metric")







            //notification
            val i = Intent(context,AlertsFragment::class.java)
            intent!!.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val alertId=intent.getIntExtra("alert",0)
            val pendingIntent = PendingIntent.getActivity(context,alertId,i,PendingIntent.FLAG_MUTABLE)

            val builder = NotificationCompat.Builder(context,"myChannel")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(response.timezone)
                .setContentText(response.current.weather.get(0).description)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)


            val notificationManager = NotificationManagerCompat.from(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            notificationManager.notify(alertId,builder.build())




            //alert
            /*var mp = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
            mp.isLooping = true
            mp.start() */


        }


    }

}