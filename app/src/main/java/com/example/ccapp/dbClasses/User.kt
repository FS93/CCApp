package com.example.ccapp.dbClasses

class User {
    var userID: String? = null
    var name: String? = null
    var surname: String? = null
    var email: String? = null
//    var pictureUrl: String? = null
    var averageReview: Float = 0F
    var numerOfReviews: Int = 0
    var rides: ArrayList<String> = ArrayList()

    constructor(){ }


    constructor(
        userID: String?,
        name: String?,
        surname: String?,
        email: String?,
//        pictureUrl: String?,
        averageReview: Float,
        numerOfReviews: Int
    ) {
        this.userID = userID
        this.name = name
        this.surname = surname
        this.email = email
//        this.pictureUrl = pictureUrl
        this.averageReview = averageReview
        this.numerOfReviews = numerOfReviews
    }


}