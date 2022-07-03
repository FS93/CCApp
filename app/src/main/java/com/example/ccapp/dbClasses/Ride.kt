package com.example.ccapp.dbClasses

import android.util.Log
import com.google.firebase.database.*

class Ride {
    var driverId: String? = null
    var departure: String? = null
    var destination: String? = null
    var price: Float? = null
    var date: String? = null
    var time: String? = null
    var seats: Int = 0
    var id: String? = null
    var passengers: ArrayList<String> = ArrayList()

    var driverName: String? = null
    var driverSurname: String? = null

    constructor() {
    }

    override fun toString(): String {
        return "Ride(driverId=$driverId, departure=$departure, destination=$destination, price=$price, date=$date, time=$time, seats=$seats, driverName=$driverName, driverSurname=$driverSurname)"
    }


}