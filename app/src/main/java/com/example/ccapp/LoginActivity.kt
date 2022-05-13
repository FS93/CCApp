package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
//import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var edt_email: EditText
    private lateinit var edt_password: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    //private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //TODO("Missing firebase integration")

        edt_email = findViewById(R.id.edt_email)
        edt_password = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        //mAuth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener{
            Toast.makeText(applicationContext,"Signup activity not yet implemented...",Toast.LENGTH_SHORT).show()

            //val intent = Intent(this, SignUp::class.java)
            //finish()
            //startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        finish()
        startActivity(intent)
//        mAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val intent = Intent(this@Login, MainActivity::class.java)
//                    finish()
//                    startActivity(intent)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("LOGIN FAILURE", "signInWithEmail:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
    }
}