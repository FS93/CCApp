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
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*

class RideDialogActivity : AppIntro2() {

    private var ride: Ride = Ride()
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef : DatabaseReference
    private lateinit var mDbRef: DatabaseReference

    private var submittable = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStatusBar(true)

        isWizardMode = true
        ride.driverId = FirebaseAuth.getInstance().currentUser?.uid!!

        database =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("ride").child("ride"+FirebaseAuth.getInstance().currentUser?.uid!!+ Calendar.getInstance().time.toString())

        mDbRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/").getReference("user/" + FirebaseAuth.getInstance().currentUser?.uid!!)
        mDbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ride", snapshot.getValue(User::class.java)!!.name.toString())
                ride.driverName = snapshot.getValue(User::class.java)!!.name.toString()
                ride.driverSurname = snapshot.getValue(User::class.java)!!.surname.toString()
                ride.driverReview = snapshot.getValue(User::class.java)!!.averageReview
                ride.id = "ride" +FirebaseAuth.getInstance().currentUser?.uid!!+ Calendar.getInstance().time.toString()
                submittable = true
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

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
        if (submittable){
            myRef.setValue(ride).addOnSuccessListener{
                Toast.makeText(
                    baseContext, "SAVED TO DB",
                    Toast.LENGTH_LONG
                ).show()
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