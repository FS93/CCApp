package com.example.ccapp

class Ride {
    var driverId: String? = null
    var departure: String? = null
    var destination: String? = null
    var rating: Float = 2.5F
    var price: Float?  = null
    var dateTime: String? = null

    constructor() {}

    constructor(
        driverId: String?,
        departure: String?,
        destination: String?,
        rating: Float,
        price: Float?,
        dateTime: String?
    ) {
        this.driverId = driverId
        this.departure = departure
        this.destination = destination
        this.rating = rating
        this.price = price
        this.dateTime = dateTime

    }
}