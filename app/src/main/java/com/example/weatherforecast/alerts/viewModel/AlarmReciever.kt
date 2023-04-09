package com.example.weatherforecast.alerts.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.NetworkConnection
import com.example.weatherforecast.R
import com.example.weatherforecast.alerts.view.AlertsFragment
import com.example.weatherforecast.dataBase.LocalRepository
import com.example.weatherforecast.generalRepository.Repository
import com.example.weatherforecast.network.WeatherClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class AlarmReciever: BroadcastReceiver() {
    var LAYOUT_FLAG = 0
    lateinit var description:String
    lateinit var area:String
    @SuppressLint("SuspiciousIndentation")
    override fun onReceive(context: Context?, intent: Intent?) {

        val repo =   Repository.getInstance(
            WeatherClient.getInstance(),
            LocalRepository.getInstance(context!!)
        )
        val sharedPref = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val latitude = sharedPref.getFloat("lat", 0f).toDouble()
        val longitude = sharedPref.getFloat("lon", 0f).toDouble()
        val units = sharedPref.getString("units", "metric").toString()
        val language = sharedPref.getString("language", "en").toString()
        val notificatioStatus=sharedPref.getString("notification_status","on").toString()
        Log.i("noti",notificatioStatus)
        if(notificatioStatus.equals("on")) {

            if (NetworkConnection.getConnectivity(context)) {
                Log.i("noti", "inside corotuine")
                CoroutineScope(Dispatchers.IO).launch {
                     repo.RS.getCurrentWeather(latitude, longitude, language, units).collect{
                         if (it.alerts.isEmpty() || it.alerts.size.equals(0)) {
                             description = context.getString(R.string.alert_dialogg_message)
                         } else {
                             description = it.alerts.get(0).description.toString()
                         }
                         area=it.timezone
                     }
                    val alertType = intent?.getStringExtra("type")
                    if (alertType.equals("Alarm")) {
                        setAlarm(context, description)
                    } else {

                        //notification
                        val i = Intent(context, AlertsFragment::class.java)
                        intent!!.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        val alertId = intent.getIntExtra("alert", 0)
                        val pendingIntent =
                            PendingIntent.getActivity(
                                context,
                                alertId,
                                i,
                                PendingIntent.FLAG_MUTABLE
                            )

                        val builder = NotificationCompat.Builder(context, "myChannel")
                            .setSmallIcon(R.drawable.icon)
                            .setContentTitle(area)
                            .setContentText(description)
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
                        notificationManager.notify(alertId, builder.build())
                        Log.i("time", "$alertId   notification id")

                    }


                }
            }else{

                CoroutineScope(Dispatchers.IO).launch {

                    val alertType = intent?.getStringExtra("type")
                    if (alertType.equals("Alarm")) {
                        setAlarm(context, "No internet connection to load you weather data")
                    } else {

                        //notification
                        val i = Intent(context, AlertsFragment::class.java)
                        intent!!.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        val alertId = intent.getIntExtra("alert", 0)
                        val pendingIntent =
                            PendingIntent.getActivity(
                                context,
                                alertId,
                                i,
                                PendingIntent.FLAG_MUTABLE
                            )

                        val builder = NotificationCompat.Builder(context, "myChannel")
                            .setSmallIcon(R.drawable.icon)
                            .setContentTitle("Error")
                            .setContentText("No internet connection to load you weather data")
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
                        notificationManager.notify(alertId, builder.build())
                        Log.i("time", "$alertId   notification id")

                    }

                }
            }




        } else{
            Log.i("noti","outside corotuine")
        }

        var id=intent?.getIntExtra("id",0)
        if (id!=0){
            CoroutineScope(Dispatchers.IO).launch{
                repo.LS.deleteAlert(MyUserAlert(10L,10L,20L,20L,"","",id!!))
            }
        }


    }
    @SuppressLint("MissingInflatedId")
    private suspend fun setAlarm(context: Context, description : String) {
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            WindowManager.LayoutParams.TYPE_PHONE


        val mp = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)

        val view: View = LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout, null, false)
        val dismissBtn = view.findViewById(R.id.alarm_button) as Button
        val textView = view.findViewById(R.id.alarm_text) as TextView
        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        layoutParams.gravity = Gravity.TOP

        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager

        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE
            textView.text = description
        }

        mp.start()
        mp.isLooping = true
        dismissBtn.setOnClickListener {
            mp?.release()
            windowManager.removeView(view)
        }

    }

}




class DeleteAlarmReciever: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val alertId=intent?.getIntExtra("alert",0)
        Log.i("time","$alertId  id in reciver")
        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.cancel(alertId!!)
    }

}


class DeleteAlarmNotificationReciever:BroadcastReceiver(){
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout, null, false)
        val windowManager=context?.getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeView(view)
    }

}