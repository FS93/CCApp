package com.example.ccapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.R
import com.example.ccapp.dbClasses.Review
import com.example.ccapp.dbClasses.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_review.view.*
import kotlinx.android.synthetic.main.item_review.view.txPersonName
import java.io.File
import java.util.*

class ReviewAdapter(var reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review,parent,false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.itemView.apply {
            txPersonName.text = reviews[position].personName
            //set default image
            ivAvatarReview.setImageResource(reviews[position].avatar)
        }

        //download the profile picture of the user
        var imageRef = FirebaseDatabase.getInstance("https://ccapp-22f27-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("user").child(reviews[position].userId!!)
        imageRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(User::class.java)!!
                if (!user.pictureUrl.isNullOrBlank()){
                    var ref = FirebaseStorage.getInstance().reference.child(user.pictureUrl!!)
                    var localImage = File.createTempFile("profile"+getRandomString(5), "jpg")
                    ref.getFile(localImage).addOnSuccessListener {
                        holder.itemView.ivAvatarReview.setImageURI(localImage.toUri())
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    override fun getItemCount(): Int {
        return reviews.size
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