package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_open_reviews.*

class OpenReviewsActivity : AppCompatActivity() {

    private lateinit var rideAdapter: RideAdapter
    private lateinit var pastRidesList: ArrayList<Ride>
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_reviews)

        pastRidesList = ArrayList()

        rideAdapter = RideAdapter(this, pastRidesList)
        rvPastRides.layoutManager = LinearLayoutManager(this)
        rvPastRides.adapter = rideAdapter

        mDbRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("ride")


        var userRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user").child(FirebaseAuth.getInstance().currentUser?.uid!!)

        userRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var userReviewedRides = snapshot.getValue(User::class.java)!!.reviewedRides
                Log.d("reviews", userReviewedRides.toString())

                mDbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        pastRidesList.clear()
                        for (postSnapshot in snapshot.children) {
                            Log.d("reviews", postSnapshot.getValue(Ride::class.java)!!.toString())
                            var ride = postSnapshot.getValue(Ride::class.java)!!
                            Log.d("reviews", ride.id!!)
                            if (ride.passengers.contains(FirebaseAuth.getInstance().currentUser?.uid!!) || ride.driverId == FirebaseAuth.getInstance().currentUser?.uid!!
                            ) {
                                if (!userReviewedRides.contains(ride.id)) pastRidesList.add(postSnapshot.getValue(Ride::class.java)!!)
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

        rideAdapter.onItemClick = { ride ->
            val intent = Intent(this@OpenReviewsActivity, RideRecordActivity::class.java)
            intent.putExtra("ride_id", ride.id)
            intent.putExtra("review", true)
            if (ride.driverId == FirebaseAuth.getInstance().currentUser?.uid!!) {
                intent.putExtra("user_type", "driver")
            } else {
                intent.putExtra("user_type", "passenger")
            }
            startActivity(intent)
        }
    }
}