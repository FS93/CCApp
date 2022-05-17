package com.example.ccapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_passenger_checkbox.view.*
import kotlinx.android.synthetic.main.item_review.view.*

class PassengerAdapter(var passengers: List<Passenger>) : RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder>(){


    inner class PassengerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_passenger_checkbox,parent,false)
        return PassengerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {


        holder.itemView.apply {
            passengerCheckbox.text = passengers[position].name
            passengerCheckbox.isSelected = passengers[position].seatTaken
        }
    }

    override fun getItemCount(): Int {
        return passengers.size
    }


}