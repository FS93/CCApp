package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {

    private lateinit var btnSaveReviews: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        btnSaveReviews = findViewById(R.id.btnSaveReviews)

        var reviewsList = mutableListOf(
            Review("Paolo Rossi", R.drawable.avatar),
            Review("Max Mustermann", R.drawable.avatar),
            Review("Mario Draghi", R.drawable.avatar),
            Review("Olaf Scholz", R.drawable.avatar)
        )

        val adapter = ReviewAdapter(reviewsList)
        rvReviews.layoutManager = LinearLayoutManager(this)
        rvReviews.adapter = adapter

        btnSaveReviews.setOnClickListener {
            rvReviews.isInvisible = true
            btnSaveReviews.isInvisible = true
            Toast.makeText(this, "Thank you for your reviews :)", Toast.LENGTH_LONG).show()
            Thread.sleep(1000)
            val intent = Intent(this@ReviewActivity, OpenReviewsActivity::class.java)
            finish()
            startActivity(intent)
        }

    }
}