package com.example.ccapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ccapp.R
import com.example.ccapp.RideDialogActivity
import com.github.appintro.SlidePolicy
import java.text.SimpleDateFormat
import java.util.*

class RideDialogFragment2 : Fragment(), SlidePolicy {

    private lateinit var edtTime: EditText
    private lateinit var datePicker: CalendarView


    override val isPolicyRespected: Boolean
        get() = check()

    private fun check(): Boolean{
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val selectedDate: String = sdf.format(Date(datePicker.date))
        if (selectedDate == "" || edtTime.text.toString() == ""){
            return false
        }
        var rda: RideDialogActivity = activity as RideDialogActivity
        rda.setDateTime(selectedDate, edtTime.text.toString())
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtTime =  view.findViewById(R.id.editTextTime)
        datePicker = view.findViewById(R.id.calendarView)
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