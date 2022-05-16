package com.example.ccapp

import android.widget.ImageView

class Ride {
    var driverName: String? = null
    var departure: String? = null
    var destination: String? = null
    var avatar: Int? = null
    var rating: Float = 2.5F
    var price: Int?  = null

    constructor() {}

    constructor(
        driverName: String?,
        departure: String?,
        destination: String?,
        avatar: Int?,
        rating: Float,
        price: Int?
    ) {
        this.driverName = driverName
        this.departure = departure
        this.destination = destination
        this.avatar = avatar
        this.rating = rating
        this.price = price
    }
}