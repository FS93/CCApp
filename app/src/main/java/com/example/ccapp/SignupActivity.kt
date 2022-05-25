package com.example.ccapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_profile.*

class SignupActivity : AppCompatActivity() {

    lateinit var profilePicture: ImageView
    lateinit var btnSignup: Button
    val pickImage = 0
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Gallery Picker for profile picture
        profilePicture = findViewById(R.id.ivProfilePicture)
        profilePicture.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        btnSignup = findViewById(R.id.btnSignUp)
        btnSignup.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            finish()
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            profilePicture.setImageURI(imageUri)
        }
    }
}