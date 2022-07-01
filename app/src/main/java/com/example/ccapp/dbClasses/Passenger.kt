package com.example.ccapp.dbClasses

class Passenger {

    constructor()

    constructor(name: String?) {
        this.name = name
        this.seatTaken = true
    }

    constructor(name: String?, userID: String?) {
        this.name = name
        this.userID = userID
        this.seatTaken = true
    }

    constructor(seatTaken: Boolean?) {
        this.name = "Available"
        this.seatTaken = false
    }


    var name: String? = null
    var userID: String? = null
    var seatTaken: Boolean = false
}