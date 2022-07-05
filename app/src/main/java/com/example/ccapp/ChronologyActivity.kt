package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccapp.Adapters.RideAdapter
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chronology.*

class ChronologyActivity : AppCompatActivity() {
    private lateinit var rideAdapter: RideAdapter
    private lateinit var ridesList: ArrayList<Ride>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chronology)

        ridesList = ArrayList()

        rideAdapter = RideAdapter(this, ridesList)
        rvChronRide.layoutManager = LinearLayoutManager(this)
        rvChronRide.adapter = rideAdapter

        mDbRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("ride")

        var userID = intent.getStringExtra("userID")!!

        //fetches all of the rides from the db and check if userID has been part of
        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ridesList.clear()
                for (postSnapshot in snapshot.children) {
                    var ride = postSnapshot.getValue(Ride::class.java)!!
                    if ( ride.passengers.contains(userID) || ride.driverId == userID ) {
                        ridesList.add(postSnapshot.getValue(Ride::class.java)!!)
                    }
                }
                rideAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}