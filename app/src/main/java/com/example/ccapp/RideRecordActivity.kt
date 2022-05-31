package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccapp.dbClasses.Passenger
import com.example.ccapp.dbClasses.Ride
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_ride_record.*

class RideRecordActivity : AppCompatActivity() {

    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_record)

        val intent: Intent = getIntent()
        val id = intent.getStringExtra("ride_id")

        val departure = findViewById<TextView>(R.id.txtRideRecordDeparture)
        val destination = findViewById<TextView>(R.id.txtRideRecordDestination)
        val driver = findViewById<TextView>(R.id.txtRideRecordDriverName)
        val dateTime = findViewById<TextView>(R.id.txtRideRecordDateTime)
        val price = findViewById<TextView>(R.id.txtRideRecordPrice)

        // Insert line breaks for long texts
        departure.text = departure.text.toString().replace(',','\n')
        destination.text = destination.text.toString().replace(',','\n')

        // Create LinearLayoutManager without horizontal scrolling
        val layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvPassengers.layoutManager = layoutManager

        var PassengerList = mutableListOf<Passenger>(
                Passenger("Francesca Maria"),
                Passenger("Giulio"),
                Passenger(false),
                Passenger(false)
            )

        mDbRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/").getReference("ride/" + id!!)
        mDbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ride", snapshot.toString())
                val ride = snapshot.getValue(Ride::class.java)!!
                departure.text = ride.departure
                destination.text = ride.destination
                driver.text = ride.driverName + " " + ride.driverSurname
                dateTime.text = ride.date + " " + ride.time
                price.text = ride.price.toString() + " €"
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


            val adapter = PassengerAdapter(PassengerList)
            rvPassengers.adapter = adapter



    }



}
