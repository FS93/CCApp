package com.example.ccapp.dbClasses

class PassengerRideRelationship {

    var rideId: String? = null
    var passengerId: String? = null

    constructor(rideId: String, passengerId: String){
        this.rideId = rideId
        this.passengerId = passengerId
    }
}