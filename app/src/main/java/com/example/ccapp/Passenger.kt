package com.example.ccapp

class Passenger {

    constructor()

    constructor(name: String?) {
        this.name = name
        this.seatTaken = true
    }

    constructor(seatTaken: Boolean?) {
        this.name = "Available"
        this.seatTaken = false
    }


    var name: String? = null
    var seatTaken: Boolean = false
}