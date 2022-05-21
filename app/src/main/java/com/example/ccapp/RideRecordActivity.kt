package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_ride_record.*
import kotlinx.android.synthetic.main.item_passenger_checkbox.*
import kotlinx.android.synthetic.main.item_passenger_checkbox.view.*
import java.util.regex.Pattern

class RideRecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_record)

        val departure = findViewById<TextView>(R.id.txtRideRecordDeparture)
        val destination = findViewById<TextView>(R.id.txtRideRecordDestination)

        // Insert line breaks for long texts
            departure.text = departure.text.toString().replace(',','\n')
            destination.text = destination.text.toString().replace(',','\n')

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
