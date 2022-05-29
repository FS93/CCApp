package com.example.ccapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {

    lateinit var profilePicture: ImageView
    private lateinit var edt_email: EditText
    private lateinit var edt_password: EditText
    private lateinit var edt_name: EditText
    private lateinit var edt_surname: EditText
    lateinit var btnSignup: Button
    val pickImage = 0
    private var imageUri: Uri? = null
    private lateinit var mAuth: FirebaseAuth

    private val SELECT_PICTURE = 200

    private lateinit var mDbRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = Firebase.auth

        // Gallery Picker for profile picture
        profilePicture = findViewById(R.id.ivProfilePicture)
        profilePicture.setOnClickListener {
//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            startActivityForResult(gallery, pickImage)

//            val i = Intent()
//            i.type = "image/*"
//            i.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
        }
        edt_email = findViewById(R.id.edt_email)
        edt_password = findViewById(R.id.edt_password)
        edt_name = findViewById(R.id.edt_name)
        edt_surname = findViewById(R.id.edt_surname)

        btnSignup = findViewById(R.id.btnSignUp)
        btnSignup.setOnClickListener {

            val name = edt_name.text.toString()
            val surname = edt_surname.text.toString()
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()
            // validity check
            if (email != "" && password != "" && surname != "" && name != "") {
                signup(name, surname, email, password)
            } else {
                Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
//            imageUri = data?.data
//            profilePicture.setImageURI(imageUri)

//            Log.d("image", resultCode.toString())
//            if (resultCode === RESULT_OK) {
//
//                // compare the resultCode with the
//                // SELECT_PICTURE constant
//                if (requestCode === SELECT_PICTURE) {
//                    // Get the url of the image from data
//                    val selectedImageUri: Uri? = data?.data
//                    if (null != selectedImageUri) {
//                        // update the preview image in the layout
//                        profilePicture.setImageURI(selectedImageUri)
//                    }
//                }
//            }
        }
    }

    private fun signup(name: String, surname: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, surname, email, mAuth.currentUser?.uid!!)
                    // Sign in success, go to HomeActivity
                    Log.d("SignUp", "createUserWithEmail:success")
                    val intent = Intent(this, HomeActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign up fails, display a message to the user.
                    Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, surname: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/").reference
        mDbRef.child("user").child(uid).setValue(User(uid, name, surname, email, 0F, 0))
    }

}









