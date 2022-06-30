package com.example.ccapp

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_passenger_checkbox.view.*
import com.example.ccapp.dbClasses.Passenger
import com.example.ccapp.dbClasses.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class PassengerAdapter(var passengers: List<Passenger>) :
    RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder>() {

    inner class PassengerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_passenger_checkbox, parent, false)

        return PassengerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {


        holder.itemView.apply {
            passengerName.text = passengers[position].name
        }

        if (passengers[position].seatTaken) {

                // -------- REPLACE WITH USER'S PROFILE PICTURE ------------

                var userRefImage =
                    FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("user")
                userRefImage.child(passengers[position].userID!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var user = snapshot.getValue(User::class.java)!!
                            if (!user.pictureUrl.isNullOrBlank()){
                                var ref = FirebaseStorage.getInstance().reference.child(user.pictureUrl!!)
                                var localImage = File.createTempFile("profile", "jpg")
                                ref.getFile(localImage).addOnSuccessListener {
                                    holder.itemView.ivPassenger.setImageURI(localImage.toUri())
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
        } else {

            holder.itemView.ivPassenger.apply {
                setImageResource(R.drawable.ic_car_gray)
            }

        }
    }

    override fun getItemCount(): Int {
        return passengers.size
    }

}


