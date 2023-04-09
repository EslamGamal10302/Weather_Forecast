package com.example.weatherforecast.alerts.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.R
import com.example.weatherforecast.alerts.viewModel.AlertOnClickListner
import com.example.weatherforecast.databinding.UserAlertListBinding
import com.example.weatherforecast.favorites.view.FavoriteAdapter
import java.text.SimpleDateFormat
import java.util.Calendar

class AlertAdapter(var context: Context,var listner:AlertOnClickListner,var userAlerts:List<MyUserAlert>): RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {
    lateinit var binding: UserAlertListBinding
    class AlertViewHolder(var binding: UserAlertListBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= DataBindingUtil.inflate(inflater, R.layout.user_alert_list,parent,false)
        return AlertViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return userAlerts.size
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        var alert=userAlerts[position]
        var calendar=Calendar.getInstance()



        calendar.timeInMillis=alert.dateFrom
        val dayFrom = SimpleDateFormat("dd").format(calendar.time)
        val monthFrom= SimpleDateFormat("MM").format(calendar.time)
        val yearFrom= SimpleDateFormat("yyyy").format(calendar.time)
        holder.binding.dateFrom.text="$dayFrom/$monthFrom/$yearFrom"

        calendar.timeInMillis=alert.dateTo
        val dayTo = SimpleDateFormat("dd").format(calendar.time)
        val monthTo= SimpleDateFormat("MM").format(calendar.time)
        val yearTo= SimpleDateFormat("yyyy").format(calendar.time)
        holder.binding.dateTo.text="$dayTo/$monthTo/$yearTo"


        calendar.timeInMillis=alert.timeFrom
        val timeFrom = SimpleDateFormat("hh:mm aa").format(calendar.time)
        holder.binding.hourFrom.text=timeFrom

        calendar.timeInMillis=alert.timeTo
        val timeTo = SimpleDateFormat("hh:mm aa").format(calendar.time)
        holder.binding.hourTo.text=timeTo

        holder.binding.alertEventTxt.text=alert.type

        holder.binding.delete.setOnClickListener {
            val yes = context.getString(R.string.answer_yes)
            val no = context.getString(R.string.answer_no)
            val message = context.getString(R.string.alert_dialog_message)
            val title = context.getString(R.string.alert_dialog_title)
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
            builder.setTitle(title)
            builder.setCancelable(false)
            builder.setPositiveButton(Html.fromHtml("<font color='#0f4cbd'>$yes</font>"),
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    listner.deleteAlert(alert)
                    Toast.makeText(
                        context,
                        context.getString(R.string.remove_alert),
                        Toast.LENGTH_SHORT
                    ).show()
                } as DialogInterface.OnClickListener)
            builder.setNegativeButton(Html.fromHtml("<font color='#0f4cbd'>$no</font>"),
                DialogInterface.OnClickListener { dialog: DialogInterface, which: Int -> dialog.cancel() } as DialogInterface.OnClickListener)

            val alertDialog = builder.create()

            alertDialog.window?.setBackgroundDrawableResource(R.drawable.settingselectors)

            alertDialog.show()
        }
    }
}