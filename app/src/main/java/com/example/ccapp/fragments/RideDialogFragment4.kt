package com.example.ccapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.ccapp.R
import com.example.ccapp.RideDialogActivity
import com.github.appintro.SlidePolicy

class RideDialogFragment4 : Fragment(), SlidePolicy {

    private lateinit var edtSeats: EditText

    override val isPolicyRespected: Boolean
        get() = check()

    private fun check(): Boolean{
        if (edtSeats.text.toString() == ""){
            return false
        }
        var rda: RideDialogActivity = activity as RideDialogActivity
        rda.setSeats(edtSeats.text.toString().toInt())
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        edtSeats = view.findViewById(R.id.edt_seats)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_dialog4, container, false)
    }

    override fun onUserIllegallyRequestedNextPage() {
        Toast.makeText(activity,"You need to fill in the information needed to go forward!",
            Toast.LENGTH_LONG).show()
    }
}