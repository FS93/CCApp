package com.example.ccapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ccapp.dbClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.ByteArrayOutputStream
import java.util.*


class SignupActivity : AppCompatActivity() {

    lateinit var profilePicture: ImageView
    private lateinit var edt_email: EditText
    private lateinit var edt_password: EditText
    private lateinit var edt_name: EditText
    private lateinit var edt_surname: EditText
    private lateinit var edt_telephone: EditText
    lateinit var btnSignup: Button

    val REQUEST_CODE = 100
    var imageUrl: String? = null
    private lateinit var bitmap: Bitmap
    private lateinit var baos: ByteArrayOutputStream
    private lateinit var uploadTask: UploadTask
    private var imagePicked = false

    // Firebase
    private lateinit var mDbRef: DatabaseReference
    lateinit var imagesRef: StorageReference
    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        edt_email = findViewById(R.id.edt_email)
        edt_password = findViewById(R.id.edt_password)
        edt_name = findViewById(R.id.edt_name)
        edt_surname = findViewById(R.id.edt_surname)
        edt_telephone = findViewById(R.id.edt_telephone)

        mAuth = Firebase.auth

        profilePicture = findViewById(R.id.ivProfilePicture)

        // on click on the image, open the Android Image Picker
        profilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        val storage = Firebase.storage
        val storageRef = storage.reference

        // generate URL for profile picture &
        imageUrl = "images/"+getRandomString(20)
        imagesRef = storageRef.child(imageUrl!!)

        btnSignup = findViewById(R.id.btnSignUp)
        btnSignup.setOnClickListener {

            val name = edt_name.text.toString()
            val surname = edt_surname.text.toString()
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()
            var telephone = edt_telephone.text.toString()
            // completeness check
            if (email != "" && password != "" && surname != "" && name != "") {
                signup(name, surname, telephone, email, password)
            } else {
                Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_SHORT).show()
            }

        }

    }

    // function that is called when an image is chosen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            ivProfilePicture.setImageURI(data?.data)

            profilePicture.isDrawingCacheEnabled = true
            profilePicture.buildDrawingCache()

            // converting image to bitmap
            bitmap = (profilePicture.drawable as BitmapDrawable).bitmap
            baos = ByteArrayOutputStream()
            imagePicked = true
        }
    }

    private fun signup(name: String, surname: String, telephone: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if(imagePicked){

                        // convert Bitmap to JPEG
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        var data = baos.toByteArray()

                        // upload image to the Storage DB
                        uploadTask = imagesRef.putBytes(data)
                        uploadTask.addOnFailureListener {
                            Toast.makeText(this, "upload failed", Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener { taskSnapshot ->
                            Toast.makeText(this, "upload successful", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        imageUrl = null
                    }
                    Log.d("SignUp", "createUserWithEmail:success")

                    // create user in the Realtime DB & use his UID form the Authentication
                    addUserToDatabase(name, surname, telephone, email, mAuth.currentUser?.uid!!, imageUrl)

                    // Sign in success, go to HomeActivity
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

    private fun addUserToDatabase(name: String, surname: String, telephone: String, email: String, uid: String, imageUri: String?) {
        mDbRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/").reference
        mDbRef.child("user").child(uid).setValue(User(uid, name, surname, telephone, email, imageUri,0F, 0, 0F, 0))
    }

    private fun getRandomString(sizeOfRandomString: Int): String? {
        val random = Random()
        val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString) sb.append(
            ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)]
            )
        return sb.toString()
    }

}









