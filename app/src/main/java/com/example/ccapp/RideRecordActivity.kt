package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_ride_record.*

class RideRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_record)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvPassengers.layoutManager = layoutManager

        var PassengerList = mutableListOf<Passenger>(
            Passenger("Francesca Maria"),
            Passenger("Giulio"),
            Passenger(false),
            Passenger(false)
        )

        val adapter = PassengerAdapter(PassengerList)

        rvPassengers.adapter = adapter


    }

}