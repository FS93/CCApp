package com.example.ccapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class DatabaseTestActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_test)

        mAuth = Firebase.auth
        login("a@b.com", "Test12")


        val database =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("rides")

//        myRef.setValue("Hello, World!2")

//        database.reference.child("rides").child("driver_id").setValue("123")

//        val ride = Ride(
//            "123uid", "Castiglione delle Stiviere, Via Roma, 1", "Brescia, Via Branze, 39",
//            4.0F, "12:00", 7
//        )
//
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // inside the method of on Data change we are setting
//                // our object class to our database reference.
//                // data base reference will sends data to firebase.
//                myRef.child("ride").setValue(ride)
//
//                // after adding this data we are showing toast message.
//                Toast.makeText(this@DatabaseTestActivity, "Data added", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // if the data is not added or it is cancelled then
//                // we are displaying a failure toast message.
//                Toast.makeText(this@DatabaseTestActivity, "Fail to add data $error", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
    }

    private fun login(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    baseContext, "Authentication success",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    baseContext, "Authentication failed: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
