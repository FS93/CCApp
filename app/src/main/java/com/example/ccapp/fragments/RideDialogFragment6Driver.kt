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


class RideDialogFragment6Driver : Fragment(), SlidePolicy {

    private lateinit var edtPrice: EditText

    override val isPolicyRespected: Boolean get() = check()

    private fun check(): Boolean {
        if(edtPrice.text.toString() == ""){
            return false
        }
        var rda: RideDialogActivity = activity as RideDialogActivity
        rda.setPrice(edtPrice.text.toString().toFloat())
        rda.saveRideToDatabase()
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtPrice = view.findViewById(R.id.edt_price)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_dialog_fragment6_driver, container, false)
    }

    override fun onUserIllegallyRequestedNextPage() {
        Toast.makeText(activity,"You need to fill in the information needed to go forward!",
            Toast.LENGTH_LONG).show()
    }

}