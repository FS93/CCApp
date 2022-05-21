package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_ride_record.*
import java.util.regex.Pattern

class RideRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_record)

        val departure = findViewById<TextView>(R.id.txtRideRecordDeparture)
        val destination = findViewById<TextView>(R.id.txtRideRecordDestination)

        // Insert line breaks for long texts
            departure.text.replace(Regex(","), "\n")
            destination.text.replace(Regex(","), "\n")

        // Setup RecyclerView for passengers

            // Create LinearLayoutManager without horizontal scrolling
            val layoutManager = object : LinearLayoutManager(this) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }

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