package com.example.ccapp

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.example.ccapp.fragments_home.CreateFragment
import com.example.ccapp.fragments_home.OpenReviewFragment
import com.example.ccapp.fragments_home.RideSearchFragment
import com.example.ccapp.fragments_home.UpcomingRidesFragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {

    // current user
    var userID = FirebaseAuth.getInstance().currentUser?.uid!!.toString()

    private lateinit var bnBottomNav: BottomNavigationView

    // fragments
    private val searchFragment = RideSearchFragment()
    private val createFragment = CreateFragment()
    private val homeFragment = UpcomingRidesFragment()
    private val openReviewFragment = OpenReviewFragment()

    // check if a given service is running - we use it for the NotificationService
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // check if NotificationService is running - if not, start it
        if (!isMyServiceRunning(NotificationService::class.java)) {
            Log.d("service", "not already running")
            val startServiceIntent = Intent(
                this@HomeActivity,
                NotificationService::class.java
            )
            startService(startServiceIntent)
        }

        // set startup fragment to homeFragment
        bnBottomNav = findViewById(R.id.bnBottomNav)
        makeCurrentFragment(homeFragment)

        bnBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.search -> {
                    makeCurrentFragment(searchFragment)
                }
                R.id.create -> {
                    makeCurrentFragment(createFragment)
                }
                R.id.home -> {
                    makeCurrentFragment(homeFragment)
                }
                R.id.review_frag -> {
                    makeCurrentFragment(openReviewFragment)
                }
            }
            true
        }

        // Database references
            // rides
        var ridesRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("ride")
            // user
        var userRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user").child(FirebaseAuth.getInstance().currentUser?.uid!!)

        // fetching number of reviews to make
            // fetch already reviewed rides of the user & compare it to all rides of his chronology,
            // i.e. rides in which he was the driver or a passenger more than 1 day ago
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // fetch already reviewed rides
                var userReviewedRides = snapshot.getValue(User::class.java)!!.reviewedRides

                // fetch all rides
                ridesRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // counter for the badge in HomeActivity
                        var counter = 0
                        var dFormat = SimpleDateFormat("dd/MM/yyyy")
                        for (rid in snapshot.children) {
                            var tmpRide = rid.getValue(Ride::class.java)!!

                            // current user was part of the ride
                            if (tmpRide.passengers.contains(userID)
                                || tmpRide.driverId == userID
                            ) {

                                var formattedDate = dFormat.parse(tmpRide.date)
                                if (System.currentTimeMillis() > formattedDate.time + 86400000 && !userReviewedRides.contains(
                                        tmpRide.id
                                    )
                                ) { // ride is more than 1 day ago & has not yet been reviewed by the current user
                                    counter++
                                }
                            }
                        }
                        addBadge(counter)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    fun addBadge(count: Int) {
        val badge: BadgeDrawable = bnBottomNav.getOrCreateBadge(
            R.id.review_frag
        )
        badge.backgroundColor = Color.YELLOW
        if (count == 0) {
            badge.isVisible = false
        } else {
            badge.number = count
            badge.isVisible = true

        }
        badge.badgeTextColor = Color.GRAY

    }

    // enable 3-Dot-Menu (top right)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    // define what happens if you click on the menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                intent.putExtra("userID", userID)
                startActivity(intent)
            }
            R.id.logout -> {
                val stopServiceIntent = Intent(
                    this@HomeActivity,
                    NotificationService::class.java
                )
                // stop NotificationService
                stopService(stopServiceIntent)
                Log.d("service", "stopped running")
                Firebase.auth.signOut()
                // go back to LoginActivity
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                finish()
                startActivity(intent)

            }
            R.id.chronology -> {
                val intent = Intent(this@HomeActivity, ChronologyActivity::class.java)
                intent.putExtra("userID", userID)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // function to change fragment
    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flWrapper, fragment)
            commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}