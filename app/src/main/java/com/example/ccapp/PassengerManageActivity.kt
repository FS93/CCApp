package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.dbClasses.Review
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_passenger_manage.*

class PassengerManageActivity : AppCompatActivity() {

    private lateinit var rideId : String
    private lateinit var rvManage: RecyclerView
    private lateinit var ride: Ride

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger_manage)
        rideId = intent.getStringExtra("ride_id").toString()

        var userList = mutableListOf<User>()
        val adapter = ManageAdapter(userList, rideId)
        rvManage = findViewById(R.id.rvManage)
        rvManage.layoutManager = LinearLayoutManager(this)
        rvManage.adapter = adapter

        dbRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("ride/" + rideId!!)
        var userRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                adapter.notifyDataSetChanged()
                ride = snapshot.getValue(Ride::class.java)!!
                for (pas in ride.passengers) {
                    userRef.child(pas)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                var passenger = dataSnapshot.getValue(User::class.java)!!
                                userList.add(passenger)
                                adapter.notifyDataSetChanged()
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}