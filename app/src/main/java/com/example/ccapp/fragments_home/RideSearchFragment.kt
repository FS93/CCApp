package com.example.ccapp.fragments_home

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.R
import com.example.ccapp.RideAdapter
import com.example.ccapp.RideRecordActivity
import com.example.ccapp.dbClasses.Ride
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class RideSearchFragment : Fragment() {

    private lateinit var resultList: RecyclerView
    private lateinit var adapter: RideAdapter
    private lateinit var rideList: ArrayList<Ride>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var edtDeparture: EditText
    private lateinit var edtDestination: EditText
    private lateinit var usrId: String

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
        return inflater.inflate(R.layout.fragment_ride_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtDeparture = view.findViewById(R.id.edt_departure_search_frag)
        edtDestination = view.findViewById(R.id.edt_destination_search_frag)
        edtDeparture.addTextChangedListener{
            updateSearch()
        }

        edtDestination.addTextChangedListener(){
            updateSearch()
        }
        resultList = view.findViewById(R.id.result_list_frag)
        rideList = ArrayList()
        adapter = RideAdapter(getActivity()!!, rideList)
        resultList.layoutManager = LinearLayoutManager(getActivity()!!)
        resultList.adapter = adapter

        mDbRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/").getReference("ride")
        usrId = FirebaseAuth.getInstance().currentUser?.uid!!
        updateSearch()

        adapter.onItemClick = { ride ->
            val intent = Intent(activity, RideRecordActivity::class.java)
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
                var dFormat = SimpleDateFormat("dd/MM/yyyy")
                for (postSnapshot in snapshot.children){
                    var ride = postSnapshot.getValue(Ride::class.java)!!
                    if (ride.passengers.size >= ride.seats){
                        continue
                    }
                    var dateOk = false
                    var formattedDate = dFormat.parse(ride.date)
                    if (System.currentTimeMillis() < formattedDate.time){
                        dateOk = true
                    }
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RideSearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}