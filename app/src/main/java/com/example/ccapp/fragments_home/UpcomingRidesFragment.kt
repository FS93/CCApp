package com.example.ccapp.fragments_home

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccapp.R
import com.example.ccapp.Adapters.RideAdapter
import com.example.ccapp.RideRecordActivity
import com.example.ccapp.dbClasses.Ride
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_upcoming_rides.*


class UpcomingRidesFragment : Fragment() {

    private lateinit var adapter: RideAdapter
    private lateinit var upcomingRideList: ArrayList<Ride>
    private lateinit var btnSearch: ImageButton
    private lateinit var btnOffer: ImageButton
    private lateinit var btnReview: ImageButton

    private lateinit var mDbRef: DatabaseReference
    var userID = FirebaseAuth.getInstance().currentUser?.uid!!.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upcoming_rides, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingRideList = ArrayList()
        adapter = RideAdapter(activity!!, upcomingRideList)
        rvUpcomingRidesFrag.layoutManager = LinearLayoutManager(activity)
        rvUpcomingRidesFrag.adapter = adapter

        mDbRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("ride")
        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                upcomingRideList.clear()
                for (postSnapshot in snapshot.children) {
                    Log.d("ride", postSnapshot.getValue(Ride::class.java)!!.toString())
                    var ride = postSnapshot.getValue(Ride::class.java)!!
                    var dFormat = SimpleDateFormat("dd/MM/yyyy")
                    var date = ride.date
                    var formattedDate = dFormat.parse(date)
                    var dateOk = false
                    if (System.currentTimeMillis() < formattedDate.time + 86400000){
                        dateOk = true
                    }

                    if ((ride.passengers.contains(userID) || ride.driverId == userID ) && dateOk) {
                        upcomingRideList.add(postSnapshot.getValue(Ride::class.java)!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        adapter.onItemClick = { ride ->
            val intent = Intent(activity, RideRecordActivity::class.java)
            intent.putExtra("ride_id", ride.id)
            intent.putExtra("userID", userID)
            if (ride.driverId == userID) {
                intent.putExtra("user_type", "driver")
            } else {
                intent.putExtra("user_type", "passenger")
            }
            startActivity(intent)
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpcomingRidesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}