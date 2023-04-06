package com.example.weatherforecast.alerts.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.R
import com.example.weatherforecast.alerts.viewModel.AlertOnClickListner
import com.example.weatherforecast.databinding.FragmentDialogAlertBinding
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

class DialogAlertFragment(var listner : AlertOnClickListner) : DialogFragment() {

    lateinit var binding: FragmentDialogAlertBinding
    lateinit var myListner: AlertOnClickListner
    var selectedAlert = MyUserAlert()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_dialog_alert, container, false)
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_alert, container, false)
        binding.lifecycleOwner
        binding.floatingDialogButton.setOnClickListener {
            dialog?.cancel()
            myListner=listner
            selectedAlert.id=generateUniqueIntValue(selectedAlert.dateFrom,selectedAlert.dateTo,"","")
            println(selectedAlert)
            myListner.onDialogSave(selectedAlert)

        }




        val calendarDateFrom = Calendar.getInstance()
        val dateFromPicker=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendarDateFrom.set(Calendar.YEAR,year)
            calendarDateFrom.set(Calendar.MONTH,month)
            calendarDateFrom.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateStartDateText(calendarDateFrom)
            var dateFromInMillisecond= calendarDateFrom.timeInMillis
            selectedAlert.dateFrom=dateFromInMillisecond
        }

        val calendarDateTo = Calendar.getInstance()
        val dateToPicker=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendarDateTo.set(Calendar.YEAR,year)
            calendarDateTo.set(Calendar.MONTH,month)
            calendarDateTo.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateEndDateText(calendarDateTo)
            var dateToInMillisecond= calendarDateTo.timeInMillis
            selectedAlert.dateTo=dateToInMillisecond

        }

        val calendarTimeStart = Calendar.getInstance()
        val timeStartPicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendarTimeStart.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendarTimeStart.set(Calendar.MINUTE,minute)
            calendarTimeStart.timeZone= TimeZone.getDefault()
            updateStartTimeText(calendarTimeStart)
            var timeStartInMillisecond= calendarTimeStart.timeInMillis
            selectedAlert.timeFrom=timeStartInMillisecond
        }


        val calendarTimeEnd = Calendar.getInstance()
        val timeEndPicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendarTimeEnd.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendarTimeEnd.set(Calendar.MINUTE,minute)
            calendarTimeEnd.timeZone= TimeZone.getDefault()
            updateEndTimeText(calendarTimeEnd)
            var timeEndInMillisecond= calendarTimeEnd.timeInMillis
            selectedAlert.timeTo=timeEndInMillisecond
        }




        binding.dateFrom.setOnClickListener {
           DatePickerDialog(requireContext(),R.style.TimePickerTheme,dateFromPicker,calendarDateFrom.get(Calendar.YEAR)
               ,calendarDateFrom.get(Calendar.MONTH),
               calendarDateFrom.get(Calendar.DAY_OF_MONTH)).show()

        }
        binding.dateTo.setOnClickListener {
            DatePickerDialog(requireContext(),R.style.TimePickerTheme,dateToPicker,
                calendarDateTo.get(Calendar.YEAR),calendarDateTo.get(Calendar.MONTH),
                calendarDateTo.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.hourFrom.setOnClickListener {
            TimePickerDialog(requireContext(),R.style.TimePickerTheme,timeStartPicker,
                calendarTimeStart.get(Calendar.HOUR_OF_DAY),calendarTimeStart.get(Calendar.MINUTE),
                false
            ).show()
        }
        binding.hourTo.setOnClickListener {
            TimePickerDialog(requireContext(),R.style.TimePickerTheme,timeEndPicker,
                calendarTimeEnd.get(Calendar.HOUR_OF_DAY),calendarTimeEnd.get(Calendar.MINUTE),
                false
            ).show()
        }

        return binding.root
    }



    private fun updateStartTimeText(calendarTime: Calendar) {
         val time = SimpleDateFormat("hh:mm aa").format(calendarTime.time)
        binding.hourFrom.text=time
    }

    private fun updateEndTimeText(calendarTime: Calendar) {
        val time = SimpleDateFormat("hh:mm aa").format(calendarTime.time)
        binding.hourTo.text=time
    }

    private fun updateStartDateText(calendarDate: Calendar) {
          val day =SimpleDateFormat("dd").format(calendarDate.time)
          val month=SimpleDateFormat("MM").format(calendarDate.time)
          val year=SimpleDateFormat("yyyy").format(calendarDate.time)

        binding.dateFrom.text="$day/$month/$year"
    }

    private fun updateEndDateText(calendarDate: Calendar) {
        val day =SimpleDateFormat("dd").format(calendarDate.time)
        val month=SimpleDateFormat("MM").format(calendarDate.time)
        val year=SimpleDateFormat("yyyy").format(calendarDate.time)

        binding.dateTo.text="$day/$month/$year"
    }

    fun generateUniqueIntValue(a: Long, b: Long, str: String, strType:String): Int {
        val input = "$a$b$str$strType"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val truncatedHash = hash.copyOfRange(0, 4) // Truncate hash to 4 bytes
        return truncatedHash.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xff) }
    }

}