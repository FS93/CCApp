package com.example.ccapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_passenger_checkbox.view.*
import com.example.ccapp.dbClasses.Passenger

class PassengerAdapter(var passengers: List<Passenger>) : RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder>(){

    // DEBUGGING: Mock-up switch for the type of user to open the activity
    enum class userType {
        DRIVER, PASSENGER
    }

    var toggleSeatChoosen: Boolean = false

    val type: userType = userType.PASSENGER

    inner class PassengerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_passenger_checkbox,parent,false)

        return PassengerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {


        holder.itemView.apply {
            passengerName.text = passengers[position].name
            passengerCheckbox.isChecked = passengers[position].seatTaken
        }


        // driver can only delete passengers, passenger can only click on available seats
        // TODO: passengers shall only be able to choose ONE seat, work with toggleSeatChoosen
        when (type) {
            userType.DRIVER -> {holder.itemView.passengerCheckbox.isClickable = passengers[position].seatTaken}
            userType.PASSENGER -> {holder.itemView.passengerCheckbox.isClickable = passengers[position].seatTaken.not()}
        }

        val cb = holder.itemView.passengerCheckbox

        cb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> run {

        // TODO: passengers shall only be able to choose ONE seat, work with toggleSeatChoosen

/*            if (type == userType.PASSENGER) {
                if (toggleSeatChoosen) {
                    if (!isChecked) {
                        toggleSeatChoosen = false
                        cb.text = "Choice undone"
                    }
                } else {
                    toggleSeatChoosen = true
                    cb.text = "Choose seat"
                }
            } else {
                if (cb.isChecked) {
                    cb.text = "Undo removal"
                } else {
                    cb.text = "Passenger removed"
                }
            }*/

            if(cb.isChecked) {
                if (type == userType.PASSENGER && !toggleSeatChoosen) {
                    cb.text = "Take Seat"
                } else {
                    cb.text = "Undo removal"
                }
            } else {
                if (type == userType.DRIVER) {
                    cb.text = "Remove Passenger"
                } else {
                    cb.text = "Undo choice"
                }
            }
        }
        })

    }

    override fun getItemCount(): Int {
        return passengers.size
    }

}


