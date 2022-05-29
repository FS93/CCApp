package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_open_reviews.*

class OpenReviewsActivity : AppCompatActivity() {

    private lateinit var rideAdapter: RideAdapter
    private lateinit var pastRidesList: ArrayList<Ride>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_reviews)

        pastRidesList = ArrayList()
//        pastRidesList.add(Ride(driverName = "Paolo", departure = "Via Branze 43, 25128 Brescia", destination = "Arthur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 10, dateTime = "17.05.2022 15:00"))
//        pastRidesList.add(Ride(driverName = "SomeoneWithALongName", departure = "Via Branze 43, 25128 Brescia", destination = "Arhtur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 100, dateTime = "17.05.2022 15:00"))
//        pastRidesList.add(Ride(driverName = "SomeoneWithALongName", departure = "Via Branze 43, 25128 Brescia", destination = "Arhtur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 100, dateTime = "17.05.2022 15:00"))

        rideAdapter = RideAdapter(this, pastRidesList)
        rvPastRides.layoutManager = LinearLayoutManager(this)
        rvPastRides.adapter = rideAdapter

        rideAdapter.onItemClick = { ride ->

            val intent = Intent(this@OpenReviewsActivity, ReviewActivity::class.java)
            startActivity(intent)
        }

    }
}