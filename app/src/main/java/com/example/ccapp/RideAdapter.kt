package com.example.ccapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ride_layout.view.*


class RideAdapter(val context: Context, val rideList: ArrayList<Ride>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RideViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtDriverName = itemView.findViewById<TextView>(R.id.txtDriverName)
        val txtDeparture = itemView.findViewById<TextView>(R.id.txtDeparture)
        val txtDestination = itemView.findViewById<TextView>(R.id.txtDestination)
        val ivAvatar = itemView.findViewById<ImageView>(R.id.ivAvatar)
        val rbDriverRating = itemView.findViewById<RatingBar>(R.id.rbDriverRating)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.ride_layout, parent, false)
        return RideViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val singleRide = rideList[position]
        var viewHolder = holder as RideViewHolder
        holder.apply {
            txtDriverName.text = singleRide.driverName
            txtDeparture.text = singleRide.departure
            txtDestination.text = singleRide.destination
            ivAvatar.setImageResource(singleRide.avatar!!)
            rbDriverRating.rating = singleRide.rating
            txtPrice.text = singleRide.price.toString() + " €"
        }


    }

    override fun getItemCount(): Int {
        return rideList.size
    }


}
