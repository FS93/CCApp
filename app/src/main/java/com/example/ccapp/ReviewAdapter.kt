package com.example.ccapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.recyclerview.widget.RecyclerView
import com.example.ccapp.dbClasses.Review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewAdapter(var reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review,parent,false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.itemView.apply {
            txPersonName.text = reviews[position].personName
            ivAvatar.setImageResource(reviews[position].avatar)
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

}