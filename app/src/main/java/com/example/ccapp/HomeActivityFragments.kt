package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.ccapp.fragments_home.CreateFragment
import com.example.ccapp.fragments_home.OpenReviewFragment
import com.example.ccapp.fragments_home.RideSearchFragment
import com.example.ccapp.fragments_home.UpcomingRidesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivityFragments : AppCompatActivity() {

    private lateinit var bnBottomNav: BottomNavigationView
    var userID = FirebaseAuth.getInstance().currentUser?.uid!!.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_fragments)

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
                Firebase.auth.signOut()
                val intent = Intent(this@HomeActivityFragments, LoginActivity::class.java)
                finish()
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
}