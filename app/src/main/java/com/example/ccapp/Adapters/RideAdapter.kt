package com.example.ccapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.R
import com.example.ccapp.dbClasses.Ride
import com.example.ccapp.dbClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class RideAdapter constructor(val context: Context, val rideList: ArrayList<Ride>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Ride) -> Unit)? = null

    inner class RideViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(rideList[absoluteAdapterPosition])
            }
        }

        // all of the fields to display
        val txtDriverName = itemView.findViewById<TextView>(R.id.txtDriverName)
        val txtDeparture = itemView.findViewById<TextView>(R.id.txtDeparture)
        val txtDestination = itemView.findViewById<TextView>(R.id.txtDestination)
        val ivAvatar = itemView.findViewById<ImageView>(R.id.ivAvatar)
        val rbDriverRating = itemView.findViewById<RatingBar>(R.id.rbDriverRating)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        val txtRideDateTextView = itemView.findViewById<TextView>(R.id.txtRideDateTime)
        val rideLayout = itemView.findViewById<ConstraintLayout>(R.id.layout_ride)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.ride_layout, parent, false)
        return RideViewHolder(view)
    }

    //function that fills all of the data
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val singleRide = rideList[position]
        holder as RideViewHolder

        //filling all of the fields with data the singleRide
        holder.apply {
            txtDriverName.text = singleRide.driverName + " " + singleRide.driverSurname
            txtDeparture.text = singleRide.departure
            txtDestination.text = singleRide.destination

            val currency = Currency.getInstance("EUR")
            val currencyFormat = java.text.NumberFormat.getCurrencyInstance(Locale.ITALY)
            currencyFormat.currency = currency
            txtPrice.text = currencyFormat.format(singleRide.price)

            txtRideDateTextView.text = singleRide.date + " " + singleRide.time

            //setting layout background with rounded corners
            if (singleRide.driverId!! == FirebaseAuth.getInstance().currentUser?.uid!!.toString()){
                rideLayout.setBackgroundResource(R.drawable.background_driver)
            } else {
                rideLayout.setBackgroundResource(R.drawable.background_passenger)
            }

            var userRef =
                FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("user")

            //fetching the data from the driver to download its image
            userRef.child(singleRide.driverId!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue(User::class.java)!!
                        rbDriverRating.rating = user.averageReviewDriver
                        if (!user.pictureUrl.isNullOrBlank()){
                            var ref = FirebaseStorage.getInstance().reference.child(user.pictureUrl!!)
                            var localImage = File.createTempFile("profile"+getRandomString(10), "jpg")
                            ref.getFile(localImage).addOnSuccessListener {
                                ivAvatar.setImageURI(localImage.toUri())
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    //function necessary for the adapter
    override fun getItemCount(): Int {
        return rideList.size
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
