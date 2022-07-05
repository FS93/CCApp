package com.example.ccapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import com.example.ccapp.dbClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var layout: ViewGroup
    private lateinit var edt_first_name: EditText
    private lateinit var edt_last_name: EditText
    private lateinit var edt_phone: EditText
    private lateinit var edt_email: EditText
    private lateinit var rtBar: RatingBar
    private lateinit var rtBarDriver: RatingBar
    private lateinit var imgView: ImageView
    private lateinit var tx_review_number: TextView
    private lateinit var tx_review_number_driver: TextView
    val REQUEST_CODE = 100
    private lateinit var bitmap: Bitmap
    private lateinit var baos: ByteArrayOutputStream
    private lateinit var uploadTask: UploadTask
    private var imagePicked = false
    lateinit var imagesRef: StorageReference
    var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val storage = Firebase.storage
        val storageRef = storage.reference

        edt_first_name = findViewById(R.id.edt_first_name)
        edt_last_name = findViewById(R.id.edt_last_name)
        edt_phone = findViewById(R.id.phone)
        edt_email = findViewById(R.id.email)
        rtBar = findViewById(R.id.ratingBar)
        rtBarDriver = findViewById(R.id.ratingBar_driver)
        tx_review_number = findViewById(R.id.tx_ratings_number)
        tx_review_number_driver = findViewById(R.id.tx_ratings_number_driver)
        imgView = findViewById(R.id.profileImage)



        var userRef =
            FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
        var userID = intent.getStringExtra("userID").toString()

        userRef.child(userID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //fill the fields
                    var user = snapshot.getValue(User::class.java)!!
                    edt_first_name.setText(user.name)
                    edt_last_name.setText(user.surname)
                    edt_phone.setText(user.telephoneNumber)
                    edt_email.setText(user.email)
                    rtBar.rating = user.averageReview
                    rtBarDriver.rating = user.averageReviewDriver
                    tx_review_number.text = "Reviews: " + user.numberOfReviews.toString()
                    tx_review_number_driver.text = "Reviews: " + user.numberOfReviewsDriver.toString()


                    //download the image
                    if (!user.pictureUrl.isNullOrBlank()){
                        imagesRef = storageRef.child(user.pictureUrl!!)
                    }
                    else imagesRef = storageRef.child("image/avatar.png")

                    if (!user.pictureUrl.isNullOrBlank()){
                        var ref = FirebaseStorage.getInstance().reference.child(user.pictureUrl!!)
                        var localImage = File.createTempFile("profile", "jpg")
                        ref.getFile(localImage).addOnSuccessListener {
                            imgView.setImageURI(localImage.toUri())
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



        //if the profile is the one of the current user he can edit his fields
        if (userID == FirebaseAuth.getInstance().currentUser?.uid!!.toString()) {
            imgView.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            // Make fields editable
            for (field in arrayListOf<EditText>(edt_first_name, edt_last_name, edt_phone)) {
                field.isEnabled = true
            }
            edt_first_name.inputType = InputType.TYPE_CLASS_TEXT
            edt_last_name.inputType = InputType.TYPE_CLASS_TEXT
            edt_phone.inputType = InputType.TYPE_CLASS_PHONE


            // Display "Save" Button
            layout = findViewById(R.id.btnProfileSave)
            val editButton = Button(this)
            editButton.text = "Save"
            layout.addView(editButton)

            // Save changes to the database
            editButton.setOnClickListener {
                userRef.child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var user = snapshot.getValue(User::class.java)!!
                            user.name = edt_first_name.text.toString()
                            user.surname = edt_last_name.text.toString()
                            user.email = edt_email.text.toString()
                            user.telephoneNumber = edt_phone.text.toString()

                            //save the user to the db
                            userRef.child(FirebaseAuth.getInstance().currentUser?.uid!!)
                                .setValue(user)

                            //if image has changed it updates it
                            if(imagePicked){
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                var data = baos.toByteArray()

                                uploadTask = imagesRef.putBytes(data)
                                uploadTask.addOnFailureListener {
                                    Toast.makeText(applicationContext, "upload failed", Toast.LENGTH_SHORT).show()
                                }.addOnSuccessListener { taskSnapshot ->
                                    Toast.makeText(applicationContext, "upload successful", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                imageUrl = null
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            imgView.setImageURI(data?.data)

            imgView.isDrawingCacheEnabled = true
            imgView.buildDrawingCache()

            bitmap = (imgView.drawable as BitmapDrawable).bitmap
            baos = ByteArrayOutputStream()
            imagePicked = true
        }
    }

}
