package com.example.ccapp.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.ProfileActivity
import com.example.ccapp.R
import kotlinx.android.synthetic.main.item_passenger_checkbox.view.*
import com.example.ccapp.dbClasses.Passenger
import com.example.ccapp.dbClasses.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

class PassengerAdapter(var passengers: List<Passenger>, var context: Context) :
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

            // Link to the Driver Profile
            holder.itemView.setOnClickListener() {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("userID", passengers[position].userID )
                //finish()
                context.startActivity(intent)
            }

            holder.itemView.ivPassenger.setImageDrawable(null)

            var userRefImage =
                FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("user")
            userRefImage.child(passengers[position].userID!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue(User::class.java)!!
                        if (!user.pictureUrl.isNullOrEmpty()) {
                            var ref =
                                FirebaseStorage.getInstance().reference.child(user.pictureUrl!!)
                            var localImage = File.createTempFile("profile"+getRandomString(5), "jpg")
                            ref.getFile(localImage).addOnSuccessListener {
                                holder.itemView.ivPassenger.setImageURI(localImage.toUri())
                            }
                        } else {
                            holder.itemView.ivPassenger.setImageResource(R.drawable.ic_car_gray)
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

    private fun getRandomString(sizeOfRandomString: Int): String? {
        val random = Random()
        val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString) sb.append(
            ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)]
        )
        return sb.toString()
    }

}


