package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.dbClasses.Review
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ReviewActivity : AppCompatActivity() {

    private lateinit var btnSaveReviews: Button
    private lateinit var ride: Ride
    private lateinit var userType: String
    private lateinit var rideId: String
    private lateinit var rvReviews: RecyclerView

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Review", "Activity Start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        userType = intent.getStringExtra("user_type").toString()
        Log.d("Review", userType)
        rideId = intent.getStringExtra("ride_id").toString()
        Log.d("Review", rideId)

        btnSaveReviews = findViewById(R.id.btnSaveReviews)

        var userList = mutableListOf<Review>()

        val adapter = ReviewAdapter(userList)
        rvReviews = findViewById(R.id.rvReviews)
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
                        if (ride.passengers.isEmpty()){
                            Toast.makeText(
                                baseContext, "Impossible to leave a review with no passengers",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this@ReviewActivity, HomeActivity::class.java)
                            finish()
                            startActivity(intent)
                        }
                        for (pas in ride.passengers) {
                            userRef.child(pas)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        Log.d("FIREBASE3", dataSnapshot.toString())
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
                                    Log.d("driver", dataSnapshot.toString())
                                    var passenger = dataSnapshot.getValue(User::class.java)!!
                                    Log.d("FIREBASE2", passenger.toString())
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

            for (user in userList){
                userRef.child(user.userId)
                    .addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var i = userList.indexOf(user)
                            var view = rvReviews.layoutManager!!.findViewByPosition(i)
                            var stars = view!!.findViewById<RatingBar>(R.id.rbReview).rating
                            var u = snapshot.getValue(User::class.java)!!

                            if(ride.driverId == u.userID){
                                u.numberOfReviewsDriver += 1
                                if(u.numberOfReviewsDriver == 1){
                                    u.averageReviewDriver = stars
                                } else {
                                    u.averageReviewDriver = (u.averageReviewDriver * (u.numberOfReviewsDriver-1) + stars) /( u.numberOfReviewsDriver)
                                }

                            } else {
                                u.numberOfReviews += 1
                                if(u.numberOfReviews == 1){
                                    u.averageReview = stars
                                } else {
                                    u.averageReview = (u.averageReview * (u.numberOfReviews-1) + stars) /( u.numberOfReviews)
                                }
                            }
                            userRef.child(user.userId).setValue(u)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }

            userRef.child(FirebaseAuth.getInstance().currentUser?.uid!!).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var currentUser = snapshot.getValue(User::class.java)!!
                    currentUser.reviewedRides.add(rideId)
                    userRef.child(FirebaseAuth.getInstance().currentUser?.uid!!).setValue(currentUser)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            Toast.makeText(this, "Thank you for your reviews :)", Toast.LENGTH_LONG).show()
            Thread.sleep(1000)
            onBackPressed()
        }

    }
}