package com.example.ccapp

import android.content.Intent
import android.icu.text.SimpleDateFormat
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
                            var ride = postSnapshot.getValue(Ride::class.java)!!
                            var userId = FirebaseAuth.getInstance().currentUser?.uid!!
                            var dFormat = SimpleDateFormat("dd/MM/yyyy")
                            var date = ride.date
                            var formattedDate = dFormat.parse(date)
                            var dateOk = false
                            if (System.currentTimeMillis() > formattedDate.time + 86400000){
                                dateOk = true
                            }
                            if (( ride.passengers.contains(userId) || ride.driverId == userId ) && dateOk) {
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