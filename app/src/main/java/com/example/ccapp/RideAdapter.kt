package com.example.ccapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.dbClasses.Ride


class RideAdapter constructor(val context: Context, val rideList: ArrayList<Ride>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Ride) -> Unit)? = null

    inner class RideViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(rideList[absoluteAdapterPosition])
            }
        }

        val txtDriverName = itemView.findViewById<TextView>(R.id.txtDriverName)
        val txtDeparture = itemView.findViewById<TextView>(R.id.txtDeparture)
        val txtDestination = itemView.findViewById<TextView>(R.id.txtDestination)
        val ivAvatar = itemView.findViewById<ImageView>(R.id.ivAvatar)
        val rbDriverRating = itemView.findViewById<RatingBar>(R.id.rbDriverRating)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        val txtRideDateTextView = itemView.findViewById<TextView>(R.id.txtRideDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.ride_layout, parent, false)
        return RideViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val singleRide = rideList[position]
        holder as RideViewHolder
        holder.apply {
            txtDriverName.text = singleRide.driverName + " " + singleRide.driverSurname
            txtDeparture.text = singleRide.departure
            txtDestination.text = singleRide.destination
            txtPrice.text = singleRide.price.toString() + " €"
            txtRideDateTextView.text = singleRide.date + " " + singleRide.time
        }
    }

    override fun getItemCount(): Int {
        return rideList.size
    }
}
