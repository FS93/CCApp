package com.example.ccapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.ccapp.fragments.*
import com.github.appintro.AppIntro2

class RideDialogActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStatusBar(true)

        isWizardMode = true

        when(intent.extras?.getString("dialog_type")) {
            "search" -> {
                addSlide(RideDialogFragment1())
                addSlide(RideDialogFragment2())
                addSlide(RideDialogFragment3())
                addSlide(RideDialogFragment5())
                addSlide(RideDialogFragment6Passenger())
            }
            "offer" -> {
                addSlide(RideDialogFragment1())
                addSlide(RideDialogFragment2())
                addSlide(RideDialogFragment3())
                addSlide(RideDialogFragment4())
                addSlide(RideDialogFragment5())
                addSlide(RideDialogFragment6Driver())
            }
        }


    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        when(intent.extras?.getString("dialog_type")) {
            "search" -> {
                val intent = Intent(this@RideDialogActivity, SearchResultActivity::class.java)
                finish()
                startActivity(intent)
            }
            "offer" -> {
                val intent = Intent(this@RideDialogActivity, ConfirmationActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }

    fun test(){
        Log.d("frag", "log from activity")
    }
}