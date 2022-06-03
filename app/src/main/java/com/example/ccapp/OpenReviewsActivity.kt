package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccapp.dbClasses.Ride
import kotlinx.android.synthetic.main.activity_open_reviews.*

class OpenReviewsActivity : AppCompatActivity() {

    private lateinit var rideAdapter: RideAdapter
    private lateinit var pastRidesList: ArrayList<Ride>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_reviews)

        pastRidesList = ArrayList()

        rideAdapter = RideAdapter(this, pastRidesList)
        rvPastRides.layoutManager = LinearLayoutManager(this)
        rvPastRides.adapter = rideAdapter

        rideAdapter.onItemClick = { ride ->

            val intent = Intent(this@OpenReviewsActivity, ReviewActivity::class.java)
            startActivity(intent)
        }

    }
}