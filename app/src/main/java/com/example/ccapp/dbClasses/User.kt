package com.example.ccapp.dbClasses

class User {
    var userID: String? = null
    var name: String? = null
    var surname: String? = null
    var email: String? = null
    var telephoneNumber: String? = null
    var pictureUrl: String? = null
    var averageReview: Float = 0F
    var averageReviewDriver: Float = 0F
    var numberOfReviews: Int = 0
    var numberOfReviewsDriver: Int = 0
    var ridesAsDriver: ArrayList<String> = ArrayList()
    var ridesAsPassenger: ArrayList<String> = ArrayList()
    var reviewedRides: ArrayList<String> = ArrayList()

    constructor(){ }


    constructor(
        userID: String?,
        name: String?,
        surname: String?,
        telephone: String?,
        email: String?,
        pictureUrl: String?,
        averageReview: Float,
        numberOfReviews: Int,
        averageReviewDriver: Float,
        numberOfReviewsDriver: Int
    ) {
        this.userID = userID
        this.name = name
        this.surname = surname
        this.email = email
        this.telephoneNumber = telephone
        this.pictureUrl = pictureUrl
        this.averageReview = averageReview
        this.numberOfReviews = numberOfReviews
        this.averageReviewDriver = averageReviewDriver
        this.numberOfReviewsDriver = numberOfReviewsDriver
    }

    override fun toString(): String {
        return "User(userID=$userID, name=$name, surname=$surname, email=$email, averageReview=$averageReview, numerOfReviews=$numberOfReviews, rides=$ridesAsDriver, rides=$ridesAsPassenger)"
    }


}