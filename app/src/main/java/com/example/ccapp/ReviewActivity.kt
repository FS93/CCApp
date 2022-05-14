package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        rvReviews.layoutManager = LinearLayoutManager(this)

        var reviewsList = mutableListOf(
            Review("Paolo Rossi", R.drawable.avatar),
            Review("Max Mustermann", R.drawable.avatar),
            Review("Mario Draghi", R.drawable.avatar),
            Review("Olaf Scholz", R.drawable.avatar),
            Review("Paolo Rossi", R.drawable.avatar),
            Review("Max Mustermann", R.drawable.avatar),
            Review("Mario Draghi", R.drawable.avatar),
            Review("Olaf Scholz", R.drawable.avatar),
            Review("Paolo Rossi", R.drawable.avatar),
            Review("Max Mustermann", R.drawable.avatar),
            Review("Mario Draghi", R.drawable.avatar),
            Review("Olaf Scholz", R.drawable.avatar)
        )

        val adapter = ReviewAdapter(reviewsList)

        rvReviews.adapter = adapter

    }
}