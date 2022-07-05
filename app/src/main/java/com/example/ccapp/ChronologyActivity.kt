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


        var userRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user").child(FirebaseAuth.getInstance().currentUser?.uid!!)

        //fetching user from database
        userRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //fetching all of the rides
                mDbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        ridesList.clear()
                        for (postSnapshot in snapshot.children) {
                            var ride = postSnapshot.getValue(Ride::class.java)!!
                            var userId = FirebaseAuth.getInstance().currentUser?.uid!!
                            if ( ride.passengers.contains(userId) || ride.driverId == userId ) {
                                ridesList.add(postSnapshot.getValue(Ride::class.java)!!)
                            }
                        }
                        rideAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}