package com.example.ccapp.dbClasses

class Notification {
    lateinit var userId: String
    lateinit var message: String

    constructor(){}

    constructor(userId: String, message: String) {
        this.userId = userId
        this.message = message
    }

    constructor(userId: String, message: String, rideId: String?) {
        this.userId = userId
        this.message = message
    }


}