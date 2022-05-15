package com.example.ccapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RideAdapter(val context: Context, val rideList: ArrayList<Ride>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RideViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtRide = itemView.findViewById<TextView>(R.id.txt_ride)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.ride_layout, parent, false)
        return RideViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rideName = rideList[position]
        var viewHolder = holder as RideViewHolder
        holder.txtRide.text = rideName.name
    }

    override fun getItemCount(): Int {
        return rideList.size
    }


}
