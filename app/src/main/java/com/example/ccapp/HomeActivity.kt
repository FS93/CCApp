package com.example.ccapp

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccapp.dbClasses.Ride
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: RideAdapter
    private lateinit var upcomingRideList: ArrayList<Ride>
    private lateinit var btnSearch: ImageButton
    private lateinit var btnOffer: ImageButton
    private lateinit var btnReview: ImageButton

    private lateinit var mDbRef: DatabaseReference

    var userID = FirebaseAuth.getInstance().currentUser?.uid!!.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnSearch = findViewById(R.id.btnSearch)
        btnOffer = findViewById(R.id.btnOffer)
        btnReview = findViewById(R.id.btnReview)

        upcomingRideList = ArrayList()
        adapter = RideAdapter(this, upcomingRideList)
        rvUpcomingRides.layoutManager = LinearLayoutManager(this)
        rvUpcomingRides.adapter = adapter

        mDbRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("ride")
        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                upcomingRideList.clear()
                for (postSnapshot in snapshot.children) {
                    Log.d("ride", postSnapshot.getValue(Ride::class.java)!!.toString())
                    var ride = postSnapshot.getValue(Ride::class.java)!!
                    var dFormat = SimpleDateFormat("dd/MM/yyyy")
                    var date = ride.date
                    var formattedDate = dFormat.parse(date)
                    var dateOk = false
                    if (System.currentTimeMillis() < formattedDate.time + 86400000){
                        dateOk = true
                    }

                    if ((ride.passengers.contains(userID) || ride.driverId == userID ) && dateOk) {
                        upcomingRideList.add(postSnapshot.getValue(Ride::class.java)!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        btnSearch.setOnClickListener {
//            val intent = Intent(this@HomeActivity, RideDialogActivity::class.java)
//            intent.putExtra("dialog_type", "search")
//            finish()
            val intent = Intent(this@HomeActivity, SearchResultActivity::class.java)
            startActivity(intent)
        }

        btnOffer.setOnClickListener {
            val intent = Intent(this@HomeActivity, RideDialogActivity::class.java)
            intent.putExtra("dialog_type", "offer")
            //finish()
            startActivity(intent)
        }

        btnReview.setOnClickListener {
            val intent = Intent(this@HomeActivity, OpenReviewsActivity::class.java)
            //finish()
            startActivity(intent)
        }

        adapter.onItemClick = { ride ->
            val intent = Intent(this@HomeActivity, RideRecordActivity::class.java)
            intent.putExtra("ride_id", ride.id)
            intent.putExtra("userID", userID)
            if (ride.driverId == userID) {
                intent.putExtra("user_type", "driver")
            } else {
                intent.putExtra("user_type", "passenger")
            }
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                intent.putExtra("userID", userID)
                //finish()
                startActivity(intent)
            }
            R.id.logout -> {
                Firebase.auth.signOut()
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                //finish()
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}