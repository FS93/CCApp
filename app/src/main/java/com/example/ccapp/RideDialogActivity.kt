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

//uses third party library AppIntro2
class RideDialogActivity : AppIntro2() {

    //empty ride to be filled as the carousel proceeds
    private var ride: Ride = Ride()
    private lateinit var database: FirebaseDatabase
    private lateinit var ridesRefDatabase : DatabaseReference
    private lateinit var userRefDatabase : DatabaseReference
    private lateinit var user : User
    var done = false

    //needed to make sure that the application has
    //the data from the user to be able to correctly
    //create the ride
    private var submittable = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStatusBar(true)

        isWizardMode = true // part of the library
        ride.driverId = FirebaseAuth.getInstance().currentUser?.uid!!

        database =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
        ridesRefDatabase = database.getReference("ride")
        userRefDatabase = database.getReference("user").child(FirebaseAuth.getInstance().currentUser?.uid!!)

        userRefDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                ride.driverName = snapshot.getValue(User::class.java)!!.name.toString()
                ride.driverSurname = snapshot.getValue(User::class.java)!!.surname.toString()
                submittable = true
                Log.d("firebase", snapshot.getValue(User::class.java)!!.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        //adding the fragments in the order to display to the user
        addSlide(RideDialogFragment1())
        addSlide(RideDialogFragment2())
        addSlide(RideDialogFragment3())
        addSlide(RideDialogFragment4())
        addSlide(RideDialogFragment5())

    }

    //gets called after the last fragment is completed
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        saveRideToDatabase()
        val intent = Intent(this@RideDialogActivity, ConfirmationActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun saveRideToDatabase(){
        if (submittable && !done) {
            //done flag is needed because the onDonePressed might be called
            //multiple times
            done = true
            submittable = false

            //get the key of the ride the app we create
            var rideRef = ridesRefDatabase.push()
            var rideKey = rideRef.key

            ride.id = rideKey
            if (rideRef == null) {
                Log.w("firebase", "Couldn't get push key for posts")
                return
            }
            rideRef.setValue(ride).addOnSuccessListener {
                user.ridesAsDriver.add(rideKey!!)
                userRefDatabase.setValue(user)
            }
        }

    }


    //these functions fill the ride as the user proceeds

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
    }

    fun setSeats(seats: Int){
        ride.seats = seats
    }

}
