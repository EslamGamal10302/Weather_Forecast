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
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentDialogAlertBinding
import java.text.SimpleDateFormat
import java.util.*

class DialogAlertFragment : DialogFragment() {

    lateinit var binding: FragmentDialogAlertBinding
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
            AlertsFragment.test=20
        }

        val calendarDate = Calendar.getInstance()
        val dateFromPicker=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendarDate.set(Calendar.YEAR,year)
            calendarDate.set(Calendar.MONDAY,month)
            calendarDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateStartDateText(calendarDate)

        }

        val dateToPicker=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendarDate.set(Calendar.YEAR,year)
            calendarDate.set(Calendar.MONDAY,month)
            calendarDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateEndDateText(calendarDate)

        }

        val calendarTime = Calendar.getInstance()
        val timeStartPicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendarTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendarTime.set(Calendar.MINUTE,minute)
            calendarTime.timeZone= TimeZone.getDefault()
            updateStartTimeText(calendarTime)
        }

        val timeEndPicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendarTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendarTime.set(Calendar.MINUTE,minute)
            calendarTime.timeZone= TimeZone.getDefault()
            updateEndTimeText(calendarTime)
        }

        binding.dateFrom.setOnClickListener {
           DatePickerDialog(requireContext(),R.style.TimePickerTheme,dateFromPicker,calendarDate.get(Calendar.YEAR),calendarDate.get(Calendar.MONTH),
               calendarDate.get(Calendar.DAY_OF_MONTH)).show()

        }
        binding.dateTo.setOnClickListener {
            DatePickerDialog(requireContext(),R.style.TimePickerTheme,dateToPicker,calendarDate.get(Calendar.YEAR),calendarDate.get(Calendar.MONTH),
                calendarDate.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.hourFrom.setOnClickListener {
            TimePickerDialog(requireContext(),R.style.TimePickerTheme,timeStartPicker,calendarTime.get(Calendar.HOUR_OF_DAY),calendarTime.get(Calendar.MINUTE),
                false
            ).show()
        }
        binding.hourTo.setOnClickListener {
            TimePickerDialog(requireContext(),R.style.TimePickerTheme,timeEndPicker,calendarTime.get(Calendar.HOUR_OF_DAY),calendarTime.get(Calendar.MINUTE),
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

}