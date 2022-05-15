package com.example.ccapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ccapp.R
import com.example.ccapp.RideDialogActivity

class RideDialogFragment1 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        Log.d("frag","on destroy called")
        super.onDestroyView()
    }

    override fun onPause() {
        Log.d("frag","on pause called")
        // send data with this method, since it is called as soon as the frag goes invisible
        var rda: RideDialogActivity = activity as RideDialogActivity
        Log.d("frag",rda.toString())
        rda.test()
        super.onPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_dialog1, container, false)
    }
}