package com.example.ccapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ccapp.fragments.*
import com.github.appintro.AppIntro2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RideDialogActivity : AppIntro2() {

    private var ride: Ride = Ride()
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStatusBar(true)

        isWizardMode = true
        ride.driverId = FirebaseAuth.getInstance().currentUser?.uid!!

        database =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("ride").child("ride"+FirebaseAuth.getInstance().currentUser?.uid!!)

        when(intent.extras?.getString("dialog_type")) {
            "search" -> {
                addSlide(RideDialogFragment1())
                addSlide(RideDialogFragment2())
                addSlide(RideDialogFragment3())
                addSlide(RideDialogFragment5())
                addSlide(RideDialogFragment6Passenger())
            }
            "offer" -> {
                addSlide(RideDialogFragment1())
                addSlide(RideDialogFragment2())
                addSlide(RideDialogFragment3())
                addSlide(RideDialogFragment4())
                addSlide(RideDialogFragment5())
                addSlide(RideDialogFragment6Driver())
            }
        }

    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        when(intent.extras?.getString("dialog_type")) {
            "search" -> {
                val intent = Intent(this@RideDialogActivity, SearchResultActivity::class.java)
                finish()
                startActivity(intent)
            }
            "offer" -> {
                val intent = Intent(this@RideDialogActivity, ConfirmationActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }

    fun saveRideToDatabase(){
        myRef.setValue(ride).addOnSuccessListener{
            Toast.makeText(
                baseContext, "SAVED TO DB",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun setDeparture(departure: String){
        ride.departure = departure
        Log.d("RIDEDIALOG", "YO WATTUP" + ride.toString())
    }

    fun setDestination(destination: String){
        ride.destination = destination
        Log.d("RIDEDIALOG", "YO WATTUP" + ride.toString())
    }

    fun setPrice(price: Float){
        ride.price = price
        Log.d("RIDEDIALOG", "YO WATTUP" + ride.toString())
    }

    fun setDateTime(date: String, time: String){
        ride.date = date
        ride.time = time
        Log.d("RIDEDIALOG", "YO WATTUP" + ride.toString())
    }

    fun setSeats(seats: Int){
        ride.seats = seats
        Log.d("RIDEDIALOG", "YO WATTUP" + ride.toString())
    }



}