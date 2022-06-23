package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.dbClasses.Ride
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*

class SearchResultActivity : AppCompatActivity() {
    private lateinit var resultList: RecyclerView
    private lateinit var adapter: RideAdapter
    private lateinit var rideList: ArrayList<Ride>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var edtDeparture: EditText
    private lateinit var edtDestination: EditText

    private lateinit var usrId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        edtDeparture = findViewById(R.id.edt_departure_search)
        edtDestination = findViewById(R.id.edt_destination_search)

        edtDeparture.addTextChangedListener{
            updateSearch()
        }

        edtDestination.addTextChangedListener(){
            updateSearch()
        }

        resultList = findViewById(R.id.result_list)
        rideList = ArrayList()
        adapter = RideAdapter(this, rideList)
        resultList.layoutManager = LinearLayoutManager(this)
        resultList.adapter = adapter

        mDbRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/").getReference("ride")
        usrId = FirebaseAuth.getInstance().currentUser?.uid!!
        updateSearch()

        adapter.onItemClick = { ride ->
            val intent = Intent(this@SearchResultActivity, RideRecordActivity::class.java)
            intent.putExtra("ride_id", ride.id)
            if (ride.driverId == FirebaseAuth.getInstance().currentUser?.uid!!) {
                intent.putExtra("user_type", "driver")
            } else {
                intent.putExtra("user_type", "passenger")
            }
            startActivity(intent)
        }
    }

    fun updateSearch(){
        var departure = edtDeparture.text.split(" ").toTypedArray()
        var destination = edtDestination.text.split(" ").toTypedArray()
        for (s in departure){
            Log.d("search", "Departure: " + s)
        }
        for (s in destination) Log.d("search", "Destination: " + s)


        mDbRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rideList.clear()
                for (postSnapshot in snapshot.children){
                    var ride = postSnapshot.getValue(Ride::class.java)!!
                    if (!(ride.passengers.contains(usrId)) && ride.driverId != usrId)
                        if(checkString(ride.departure, departure) && checkString(ride.destination, destination)) rideList.add(ride)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun checkString(text: String?, words: Array<String>?): Boolean{
        if(text.isNullOrBlank()) return true
        if(words.isNullOrEmpty()) return true
        if(text == "") return true
        for (s in words) if (text.lowercase().contains(s.lowercase())) return true
        return false
    }
}