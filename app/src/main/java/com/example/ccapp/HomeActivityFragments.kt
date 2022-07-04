package com.example.ccapp

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.DateFormat
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
import java.util.*


class HomeActivityFragments : AppCompatActivity() {


    private lateinit var bnBottomNav: BottomNavigationView
    var userID = FirebaseAuth.getInstance().currentUser?.uid!!.toString()

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
        setContentView(R.layout.activity_home_fragments)


        if(!isMyServiceRunning(NotificationService::class.java)){
            Log.d("service", "not already running")
            val startServiceIntent = Intent(
                this@HomeActivityFragments,
                NotificationService::class.java
            )
            startService(startServiceIntent)
        }

        bnBottomNav = findViewById(R.id.bnBottomNav)
        val searchFragment = RideSearchFragment()
        val createFragment = CreateFragment()
        val homeFragment = UpcomingRidesFragment()
        val openReviewFragment = OpenReviewFragment()
        makeCurrentFragment(homeFragment)

        bnBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.search -> {
                    makeCurrentFragment(searchFragment)
                }
                R.id.create -> {
                    makeCurrentFragment(createFragment)
                }
                R.id.home-> {
                    makeCurrentFragment(homeFragment)
                }
                R.id.review_frag-> {
                    makeCurrentFragment(openReviewFragment)
                }
            }
            true
        }


        var mDbRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("ride")
        var userRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user").child(FirebaseAuth.getInstance().currentUser?.uid!!)

        userRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userReviewedRides = snapshot.getValue(User::class.java)!!.reviewedRides
                Log.d("reviews", userReviewedRides.toString())

                mDbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var counter = 0
                        var dFormat = SimpleDateFormat("dd/MM/yyyy")
                        for(rid in snapshot.children){
                            var tmpRide = rid.getValue(Ride::class.java)!!
                            if (tmpRide.passengers.contains(userID)
                                || tmpRide.driverId == userID){

                                var formattedDate = dFormat.parse(tmpRide.date)
                                if (System.currentTimeMillis() > formattedDate.time + 86400000 && !userReviewedRides.contains(tmpRide.id)){
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
                TODO("Not yet implemented")
            }
        })

    }

    fun addBadge(count: Int){
        val badge: BadgeDrawable = bnBottomNav.getOrCreateBadge(
            R.id.review_frag
        )
        badge.backgroundColor = Color.YELLOW
        if(count == 0){
            badge.isVisible = false
        } else {
            badge.number = count
            badge.isVisible = true

        }
        badge.badgeTextColor = Color.GRAY

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this@HomeActivityFragments, ProfileActivity::class.java)
                intent.putExtra("userID", userID)
//                finish()
                startActivity(intent)
            }
            R.id.logout -> {
                        val stopServiceIntent = Intent(
            this@HomeActivityFragments,
            NotificationService::class.java
        )
        stopService(stopServiceIntent)
        Log.d("service", "stopped running")
                Firebase.auth.signOut()
                val intent = Intent(this@HomeActivityFragments, LoginActivity::class.java)
                finish()
                startActivity(intent)

            }
            R.id.chronology -> {
                val intent = Intent(this@HomeActivityFragments, ChronologyActivity::class.java)
                intent.putExtra("userID", userID)
//                finish()
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

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