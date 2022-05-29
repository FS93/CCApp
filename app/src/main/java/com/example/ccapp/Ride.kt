package com.example.ccapp

class Ride {
    var driverId: String? = null
    var departure: String? = null
    var destination: String? = null
    var price: Float? = null
    var date: String? = null
    var time: String? = null
    var seats: Int = 0

    constructor() {}

    constructor(
        driverId: String?,
        departure: String?,
        destination: String?,
        price: Float?,
        date: String?,
        time: String?,
        seats: Int
    ) {
        this.driverId = driverId
        this.departure = departure
        this.destination = destination
        this.price = price
        this.date = date
        this.time = time
        this.seats = seats
    }

    override fun toString(): String {
        return "{driverId: $driverId, departure: $departure, " +
                "destination: $destination, price: $price, " +
                "date: $date, time: $time, seats: $seats}"
    }
}