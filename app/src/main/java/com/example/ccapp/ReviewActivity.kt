package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccapp.dbClasses.Passenger
import com.example.ccapp.dbClasses.Review
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {

    private lateinit var btnSaveReviews: Button
    private lateinit var ride: Ride
    private lateinit var userType: String
    private lateinit var rideId: String

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        userType = intent.getStringExtra("user_type").toString()
        rideId = intent.getStringExtra("ride_id").toString()

        btnSaveReviews = findViewById(R.id.btnSaveReviews)

        var userList = mutableListOf<Review>()
//        var userList = mutableListOf(
////            Review("Paolo Rossi", R.drawable.avatar),
////            Review("Max Mustermann", R.drawable.avatar),
////            Review("Mario Draghi", R.drawable.avatar),
////            Review("Paolo Rossi", R.drawable.avatar),
////            Review("Max Mustermann", R.drawable.avatar),
////            Review("Mario Draghi", R.drawable.avatar),
//            Review("Olaf Scholz", R.drawable.avatar)
//        )

        val adapter = ReviewAdapter(userList)
        rvReviews.layoutManager = LinearLayoutManager(this)
        rvReviews.adapter = adapter

        dbRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("ride/" + rideId!!)
        var userRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                adapter.notifyDataSetChanged()
                ride = snapshot.getValue(Ride::class.java)!!
                when (userType) {
                    null -> {
                        Toast.makeText(
                            baseContext, "An error has occurred",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@ReviewActivity, HomeActivity::class.java)
                        finish()
                        startActivity(intent)
                    }
                    "driver" -> {
                        for (pas in ride.passengers) {
                            userRef.child(pas)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        Log.d("FIREBASE1", dataSnapshot.toString())
                                        var passenger = dataSnapshot.getValue(User::class.java)!!
                                        userList.add(
                                            Review(
                                                passenger.name + " " + passenger.surname,
                                                R.drawable.avatar,
                                                passenger.userID!!
                                            )
                                        )
                                        adapter.notifyDataSetChanged()
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                    }
                                })
                        }
                    }
                    "passenger" -> {
                        userRef.child(ride.driverId!!)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    Log.d("FIREBASE1", dataSnapshot.toString())
                                    var passenger = dataSnapshot.getValue(User::class.java)!!
                                    userList.add(
                                        Review(
                                            passenger.name + " " + passenger.surname,
                                            R.drawable.avatar,
                                            passenger.userID!!
                                        )
                                    )
                                    adapter.notifyDataSetChanged()
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                }
                            })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


        btnSaveReviews.setOnClickListener {
            rvReviews.isInvisible = true
            btnSaveReviews.isInvisible = true
            Toast.makeText(this, "Thank you for your reviews :)", Toast.LENGTH_LONG).show()
            Thread.sleep(1000)
            val intent = Intent(this@ReviewActivity, OpenReviewsActivity::class.java)
            finish()
            startActivity(intent)
        }

    }
}