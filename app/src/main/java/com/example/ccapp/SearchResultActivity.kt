package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.dbClasses.Ride
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

class SearchResultActivity : AppCompatActivity(), OnMapReadyCallback {

    //TODO: Google maps to be implemented later


    private lateinit var resultList: RecyclerView
    private lateinit var rideAdapter: RideAdapter
    private lateinit var rideList: ArrayList<Ride>
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

//        mapView = findViewById(R.id.mapView)
//        mapView.onCreate(savedInstanceState)
//        mapView.getMapAsync(this)

//        resultList = findViewById(R.id.result_list)

//        rideList = ArrayList()
//        rideList.add(Ride(driverName = "Paolo", departure = "Via Branze 43, 25128 Brescia", destination = "Arthur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 10, dateTime = "17.05.2022 15:00"))
//        rideList.add(Ride(driverName = "SomeoneWithALongName", departure = "Via Branze 43, 25128 Brescia", destination = "Arhtur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 100, dateTime = "17.05.2022 15:00"))
//        rideList.add(Ride(driverName = "Paolo", departure = "Via Branze 43, 25128 Brescia", destination = "Arhtur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 8, dateTime = "17.05.2022 15:00"))
//        rideList.add(Ride(driverName = "Paolo", departure = "Via Branze 43, 25128 Brescia", destination = "Arhtur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 8, dateTime = "17.05.2022 15:00"))
//        rideList.add(Ride(driverName = "Paolo", departure = "Via Branze 43, 25128 Brescia", destination = "Arhtur-Hoffmann-Str. 87, 04275 Leipzig", rating = 2.0F, avatar = R.drawable.avatar, price = 9, dateTime = "17.05.2022 15:00"))
//
//        rideAdapter = RideAdapter(this, rideList)
//
//        resultList.layoutManager = LinearLayoutManager(this)
//        resultList.adapter = rideAdapter
    }

//    override fun onStart() {
//        super.onStart()
//        mapView.onStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mapView.onStop()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView.onLowMemory()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mapView.onDestroy()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mapView.onResume()
//    }
//
    override fun onMapReady(p0: GoogleMap) {
    }
//
//    override fun onPause() {
//        super.onPause()
//        mapView.onPause()
//    }
//     override fun onSaveInstanceState(outState: Bundle){
//         super.onSaveInstanceState(outState)
//         mapView.onSaveInstanceState(outState)
//     }
}