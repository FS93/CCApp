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

//        mDbRef = FirebaseDatabase
//            .getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
//            .getReference("user/" + FirebaseAuth.getInstance().currentUser?.uid!!)
        userRefDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ride.driverName = snapshot.getValue(User::class.java)!!.name.toString()
                ride.driverSurname = snapshot.getValue(User::class.java)!!.surname.toString()
                ride.driverReview = snapshot.getValue(User::class.java)!!.averageReview
                submittable = true
                Log.d("firebase", snapshot.getValue(User::class.java)!!.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        when(intent.extras?.getString("dialog_type")) {
            "search" -> {
                addSlide(RideDialogFragment1())
                addSlide(RideDialogFragment2())
                addSlide(RideDialogFragment3())
//                addSlide(RideDialogFragment5())
                addSlide(RideDialogFragment6Passenger())
            }
            "offer" -> {
                addSlide(RideDialogFragment1())
                addSlide(RideDialogFragment2())
                addSlide(RideDialogFragment3())
                addSlide(RideDialogFragment4())
//                addSlide(RideDialogFragment5())
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
//        if (submittable){
//            ridesRefDatabase.setValue(ride).addOnSuccessListener{
//                Toast.makeText(
//                    baseContext, "SAVED TO DB",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        } else {
//            Toast.makeText(
//                baseContext, "An error has occured",
//                Toast.LENGTH_LONG
//            ).show()
//            val intent = Intent(this@RideDialogActivity, HomeActivity::class.java)
//            finish()
//            startActivity(intent)
//        }

        if (submittable) {
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
        } else {
            Toast.makeText(
                baseContext, "An error has occured",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(this@RideDialogActivity, HomeActivity::class.java)
            finish()
            startActivity(intent)
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
    }

    fun setSeats(seats: Int){
        ride.seats = seats
    }

}
