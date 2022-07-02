package com.example.ccapp.fragments_home

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.HomeActivityFragments
import com.example.ccapp.R
import com.example.ccapp.RideAdapter
import com.example.ccapp.RideRecordActivity
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_open_reviews.*

class OpenReviewFragment : Fragment() {

    private lateinit var rideAdapter: RideAdapter
    private lateinit var pastRidesList: ArrayList<Ride>
    private lateinit var mDbRef: DatabaseReference

    private lateinit var rvPastRidesView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_open_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pastRidesList = ArrayList()

        rideAdapter = RideAdapter(activity!!, pastRidesList)
        rvPastRidesView = view.findViewById(R.id.rvPastRidesFrag)
        rvPastRidesView.layoutManager = LinearLayoutManager(activity!!)
        rvPastRidesView.adapter = rideAdapter

        mDbRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("ride")

        var userRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user").child(FirebaseAuth.getInstance().currentUser?.uid!!)

        userRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userReviewedRides = snapshot.getValue(User::class.java)!!.reviewedRides
                Log.d("reviews", userReviewedRides.toString())

                mDbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        pastRidesList.clear()
                        for (postSnapshot in snapshot.children) {
                            var ride = postSnapshot.getValue(Ride::class.java)!!
                            var userId = FirebaseAuth.getInstance().currentUser?.uid!!
                            var dFormat = SimpleDateFormat("dd/MM/yyyy")
                            var date = ride.date
                            var formattedDate = dFormat.parse(date)
                            var dateOk = false
                            if (System.currentTimeMillis() > formattedDate.time + 86400000){
                                dateOk = true
                            }
                            if (( ride.passengers.contains(userId) || ride.driverId == userId ) && dateOk) {
                                if (!userReviewedRides.contains(ride.id)) pastRidesList.add(postSnapshot.getValue(Ride::class.java)!!)
                            }
                        }
                        rideAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        rideAdapter.onItemClick = { ride ->
            val intent = Intent(activity, RideRecordActivity::class.java)
            intent.putExtra("ride_id", ride.id)
            intent.putExtra("review", true)
            if (ride.driverId == FirebaseAuth.getInstance().currentUser?.uid!!) {
                intent.putExtra("user_type", "driver")
            } else {
                intent.putExtra("user_type", "passenger")
            }
            startActivity(intent)
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OpenReviewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OpenReviewFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}