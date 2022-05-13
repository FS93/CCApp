package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button

class ProfileActivity : AppCompatActivity() {

    private lateinit var layout: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        layout = findViewById(R.id.button_layout)
        val editButton = Button(this)
        editButton.setText("Edit")
        layout.addView(editButton)
    }
}