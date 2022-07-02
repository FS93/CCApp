package com.example.ccapp.fragments_ride

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ccapp.R
import com.example.ccapp.RideDialogActivity
import com.github.appintro.SlidePolicy
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat


class RideDialogFragment2 : Fragment(), SlidePolicy {

    private lateinit var edtTime: EditText
    private lateinit var datePicker: CalendarView
    private var curDate: String? = "00/00/0000"


    override val isPolicyRespected: Boolean
        get() = check()

    private fun check(): Boolean{
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val selectedDate: String? = curDate
        if (selectedDate.isNullOrEmpty() || edtTime.text.toString().isNullOrEmpty()){
            return false
        }
        var rda: RideDialogActivity = activity as RideDialogActivity
        rda.setDateTime(curDate!!, edtTime.text.toString())
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtTime = view.findViewById(R.id.editTextTime)
        datePicker = view.findViewById(R.id.calendarView)

        datePicker.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            val f: NumberFormat = DecimalFormat("00")
            curDate = dayOfMonth.toString()+"/"+f.format(month)+"/"+year
            Log.d("date", curDate!!)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_dialog2, container, false)
    }

    override fun onUserIllegallyRequestedNextPage() {
        Toast.makeText(activity,"You need to fill in the information needed to go forward!",
            Toast.LENGTH_LONG).show()
    }

}