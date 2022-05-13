package com.example.ccapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class ProfileActivity : AppCompatActivity() {

    private lateinit var layout: ViewGroup
    private lateinit var edt_first_name: EditText
    private lateinit var edt_last_name: EditText
    private lateinit var edt_phone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        edt_first_name = findViewById(R.id.edt_first_name)
        edt_last_name = findViewById(R.id.edt_last_name)
        edt_phone = findViewById(R.id.phone)

        layout = findViewById(R.id.button_layout)
        val editButton = Button(this)
        editButton.text = "Edit"
        layout.addView(editButton)

        editButton.setOnClickListener {
            // make content editable
            //TODO("make content of text views editable")

            layout.removeView(editButton)
        }
    }
}