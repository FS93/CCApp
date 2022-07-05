package com.example.ccapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.example.ccapp.fragments_ride.*
import com.github.appintro.AppIntro2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RideDialogActivity : AppIntro2() {

    private var ride: Ride = Ride()
    private lateinit var database: FirebaseDatabase
    private lateinit var ridesRefDatabase : DatabaseReference
    private lateinit var userRefDatabase : DatabaseReference
    private lateinit var mDbRef: DatabaseReference
    private lateinit var user : User
    var done = false

    private var submittable = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStatusBar(true)

        isWizardMode = true
        ride.driverId = FirebaseAuth.getInstance().currentUser?.uid!!

        database =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
        ridesRefDatabase = database.getReference("ride")
        userRefDatabase = database.getReference("user").child(FirebaseAuth.getInstance().currentUser?.uid!!)

        userRefDatabase.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                user = snapshot.getValue(User::class.java)!!
            }

            override fun onCancelled(error: DatabaseError){

            }
        })

        userRefDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ride.driverName = snapshot.getValue(User::class.java)!!.name.toString()
                ride.driverSurname = snapshot.getValue(User::class.java)!!.surname.toString()
                submittable = true
                Log.d("firebase", snapshot.getValue(User::class.java)!!.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        when(intent.extras?.getString("dialog_type")) {
            "search" -> {
            }
            "offer" -> {
                addSlide(RideDialogFragment1())
                addSlide(RideDialogFragment2())
                addSlide(RideDialogFragment3())
                addSlide(RideDialogFragment4())
//                addSlide(RideDialogFragment5())
                addSlide(RideDialogFragment5())
            }
        }

    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        when(intent.extras?.getString("dialog_type")) {
            "offer" -> {
                saveRideToDatabase()
                val intent = Intent(this@RideDialogActivity, ConfirmationActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }

    fun saveRideToDatabase(){
        if (submittable && !done) {
            done = true
            submittable = false
            var rideRef = ridesRefDatabase.push()
            var rideKey = rideRef.key
            ride.id = rideKey
            if (rideRef == null) {
                Log.w("firebase", "Couldn't get push key for posts")
                return
            }
            rideRef.setValue(ride).addOnSuccessListener {
                Toast.makeText(
                    baseContext, "SAVED TO DB",
                    Toast.LENGTH_LONG
                ).show()
                user.ridesAsDriver.add(rideKey!!)
                userRefDatabase.setValue(user)
            }
        }

    }

    fun setDeparture(departure: String){
        ride.departure = departure
    }

    fun setDestination(destination: String){
        ride.destination = destination
    }

    fun setPrice(price: Float){
        ride.price = price
    }

    fun setDateTime(date: String, time: String){
        ride.date = date
        Log.d("datetime", date)
        ride.time = time
        Log.d("datetime", time)
        Toast.makeText(this, ride.date + " " + ride.time, Toast.LENGTH_LONG).show()
    }

    fun setSeats(seats: Int){
        ride.seats = seats
    }

}
