package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.dbClasses.Ride
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*

class SearchResultActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var resultList: RecyclerView
    private lateinit var adapter: RideAdapter
    private lateinit var rideList: ArrayList<Ride>
    private lateinit var mapView: MapView
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

//        mapView = findViewById(R.id.mapView)
//        mapView.onCreate(savedInstanceState)
//        mapView.getMapAsync(this)

        resultList = findViewById(R.id.result_list)
        rideList = ArrayList()
        adapter = RideAdapter(this, rideList)
        resultList.layoutManager = LinearLayoutManager(this)
        resultList.adapter = adapter

        mDbRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/").getReference("ride")
        mDbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rideList.clear()
                for (postSnapshot in snapshot.children){
                    Log.d("ride", postSnapshot.getValue(Ride::class.java)!!.toString())
                    rideList.add(postSnapshot.getValue(Ride::class.java)!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        adapter.onItemClick = { ride ->
            val intent = Intent(this@SearchResultActivity, RideRecordActivity::class.java)
            intent.putExtra("ride_id", ride.id)
            startActivity(intent)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
    }
}