package com.example.ccapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccapp.dbClasses.Passenger
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_ride_record.*


class RideRecordActivity : AppCompatActivity() {

    private lateinit var mDbRef : DatabaseReference
    private lateinit var ride : Ride
    private lateinit var adapter : PassengerAdapter
    private lateinit var passengerList : MutableList<Passenger>
    private lateinit var btnAction : Button
    private var userType : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_record)

        val intent: Intent = getIntent()
        val id = intent.getStringExtra("ride_id")
        userType = intent.getStringExtra("user_type")
        var user: User


        val departure = findViewById<TextView>(R.id.txtRideRecordDeparture)
        val destination = findViewById<TextView>(R.id.txtRideRecordDestination)
        val driver = findViewById<TextView>(R.id.txtRideRecordDriverName)
        val dateTime = findViewById<TextView>(R.id.txtRideRecordDateTime)
        val price = findViewById<TextView>(R.id.txtRideRecordPrice)

        btnAction = findViewById(R.id.btn_action)

        passengerList = mutableListOf<Passenger>()
        adapter = PassengerAdapter(passengerList)
        rvPassengers.adapter = adapter

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

        mDbRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/").getReference("ride/" + id!!)
        mDbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                passengerList.clear()
                Log.d("ride", snapshot.toString())
                ride = snapshot.getValue(Ride::class.java)!!
                departure.text = ride.departure
                destination.text = ride.destination
                driver.text = ride.driverName + " " + ride.driverSurname
                dateTime.text = ride.date + " " + ride.time
                price.text = ride.price.toString() + " €"

                btnAction.setOnClickListener {
                    var userRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("user").child(FirebaseAuth.getInstance().currentUser?.uid!!)

                    when(userType){
                        null -> {
                            Toast.makeText(
                                baseContext, "An error has occured",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this@RideRecordActivity, HomeActivity::class.java)
                            finish()
                            startActivity(intent)
                        }
                        "driver" -> {
                            Toast.makeText(baseContext, "new activity opens", Toast.LENGTH_LONG).show()
                        }
                        "passenger" -> { //unjoin
                            if(ride.passengers.contains(FirebaseAuth.getInstance().currentUser?.uid!!)){
                                userRef.addListenerForSingleValueEvent(object: ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot){
                                        user = snapshot.getValue(User::class.java)!!
                                        user.ridesAsPassenger.remove(id)
                                        userRef.setValue(user)
                                        ride.passengers.remove(FirebaseAuth.getInstance().currentUser?.uid!!)
                                        mDbRef.setValue(ride)
                                    }

                                    override fun onCancelled(error: DatabaseError){

                                    }
                                })
                            } else { //join
                                userRef.addListenerForSingleValueEvent(object: ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot){
                                        user = snapshot.getValue(User::class.java)!!
                                        if (!user.ridesAsPassenger.contains(id)){
                                            user.ridesAsPassenger.add(id)
                                            userRef.setValue(user)
                                        }
                                        if(!ride.passengers.contains((FirebaseAuth.getInstance().currentUser?.uid!!))){
                                            ride.passengers.add(FirebaseAuth.getInstance().currentUser?.uid!!)
                                            mDbRef.setValue(ride)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError){

                                    }
                                })
                            }
                        }
                    }


                }



                if (ride.seats < ride.passengers.size) {
                    Toast.makeText(
                        baseContext, "An error has occured",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this@RideRecordActivity, HomeActivity::class.java)
                    finish()
                    startActivity(intent)
                }

                var userRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/").getReference("user")
                for (i in 1 .. (ride.seats - ride.passengers.size)) passengerList.add(Passenger(false))
                for (pas in ride.passengers) {
                    userRef.child(pas).addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            Log.d("FIREBASE1", dataSnapshot.toString())
                            var passenger = dataSnapshot.getValue(User::class.java)!!
                            passengerList.add(0, Passenger(passenger.name + " " + passenger.surname))
                            adapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                        }
                    })
                }

                when(userType){
                    null -> {
                        Toast.makeText(
                            baseContext, "An error has occured",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@RideRecordActivity, HomeActivity::class.java)
                        finish()
                        startActivity(intent)
                    }
                    "driver" -> {
                        btnAction.text = "Manage"
                    }
                    "passenger" -> {
                        if(ride.passengers.contains(FirebaseAuth.getInstance().currentUser?.uid!!)){
                            btnAction.text = "Unjoin"
                        } else {
                            btnAction.text = "Join"
                        }
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }


        })




    }

}
