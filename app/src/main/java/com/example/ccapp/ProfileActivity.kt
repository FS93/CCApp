package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import com.example.ccapp.dbClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileActivity : AppCompatActivity() {

    private lateinit var layout: ViewGroup
    private lateinit var edt_first_name: EditText
    private lateinit var edt_last_name: EditText
    private lateinit var edt_phone: EditText
    private lateinit var edt_email: EditText
    private lateinit var rtBar: RatingBar
    private lateinit var tx_review_number: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        edt_first_name = findViewById(R.id.edt_first_name)
        edt_last_name = findViewById(R.id.edt_last_name)
        edt_phone = findViewById(R.id.phone)
        edt_email = findViewById(R.id.email)
        rtBar = findViewById(R.id.ratingBar)
        tx_review_number = findViewById(R.id.tx_ratings_number)


        //Toast.makeText(this,"$userID", Toast.LENGTH_LONG).show()

        var userRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
        //userRef.child(FirebaseAuth.getInstance().currentUser?.uid!!)
        var userID = intent.getStringExtra("userID").toString()
        userRef.child(userID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var user = snapshot.getValue(User::class.java)!!
                    edt_first_name.setText(user.name)
                    edt_last_name.setText(user.surname)
                    edt_phone.setText(user.telephoneNumber)
                    edt_email.setText(user.email)
                    rtBar.rating = user.averageReview
                    tx_review_number.text = "Reviews: " + user.numerOfReviews.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        if (userID == FirebaseAuth.getInstance().currentUser?.uid!!.toString()) {

            layout = findViewById(R.id.btnProfileSave)
            val editButton = Button(this)
            editButton.text = "Save"
            layout.addView(editButton)

            editButton.setOnClickListener {
                // make content editable
                //TODO("make content of text views editable")
                userRef.child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var user = snapshot.getValue(User::class.java)!!
                            user.name = edt_first_name.text.toString()
                            user.surname = edt_last_name.text.toString()
                            user.email = edt_email.text.toString()
                            user.telephoneNumber = edt_phone.text.toString()
                            userRef.child(FirebaseAuth.getInstance().currentUser?.uid!!)
                                .setValue(user)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })


                val text = "Profile updated!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()

                finish()

//            layout.removeView(editButton)
            }
        }
    }
}