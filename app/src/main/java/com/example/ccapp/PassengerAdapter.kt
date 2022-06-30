package com.example.ccapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_passenger_checkbox.view.*
import com.example.ccapp.dbClasses.Passenger

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
            holder.itemView.ivPassenger.apply {
                // -------- REPLACE WITH USER'S PROFILE PICTURE ------------
                setBackgroundResource(R.drawable.avatar)
                }
        } else {
            holder.itemView.ivPassenger.apply {
                setBackgroundResource(R.drawable.ic_car_gray)
            }

        }
    }

    override fun getItemCount(): Int {
        return passengers.size
    }

}


