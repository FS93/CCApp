package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var upcomingAdapter: RideAdapter
    private lateinit var upcomingRideList: ArrayList<Ride>
    private lateinit var btnSearch: ImageButton
    private lateinit var btnOffer: ImageButton
    private lateinit var btnReview: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnSearch = findViewById(R.id.btnSearch)
        btnOffer = findViewById(R.id.btnOffer)
        btnReview = findViewById(R.id.btnReview)

        btnSearch.setOnClickListener{
            val intent = Intent(this@HomeActivity, RideDialogActivity::class.java)
            intent.putExtra("dialog_type", "search")
            //finish()
            startActivity(intent)
        }

        btnOffer.setOnClickListener{
            val intent = Intent(this@HomeActivity, RideDialogActivity::class.java)
            intent.putExtra("dialog_type", "offer")
            //finish()
            startActivity(intent)
        }

        btnReview.setOnClickListener{
            val intent = Intent(this@HomeActivity, OpenReviews::class.java)
            intent.putExtra("dialog_type", "offer")
            //finish()
            startActivity(intent)
        }


        upcomingRideList = ArrayList()
        upcomingRideList.add(Ride(driverName = "Paolo", departure = "Via Branze 43, 25128 Brescia", destination = "Arthur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 10, dateTime = "17.05.2022 15:00"))
        upcomingRideList.add(Ride(driverName = "SomeoneWithALongName", departure = "Via Branze 43, 25128 Brescia", destination = "Arhtur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 100, dateTime = "17.05.2022 15:00"))
        upcomingRideList.add(Ride(driverName = "Paolo", departure = "Via Branze 43, 25128 Brescia", destination = "Arthur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 10, dateTime = "17.05.2022 15:00"))
        upcomingRideList.add(Ride(driverName = "SomeoneWithALongName", departure = "Via Branze 43, 25128 Brescia", destination = "Arhtur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 100, dateTime = "17.05.2022 15:00"))


        upcomingAdapter = RideAdapter(this, upcomingRideList)
        rvUpcomingRides.layoutManager = LinearLayoutManager(this)
        rvUpcomingRides.adapter = upcomingAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.profile -> {
                val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
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