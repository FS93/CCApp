package com.example.ccapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.search -> {
                val intent = Intent(this@HomeActivity, RideDialogActivity::class.java)
                intent.putExtra("dialog_type", "search")
                //finish()
                startActivity(intent)
            }
            R.id.offer -> {
                val intent = Intent(this@HomeActivity, RideDialogActivity::class.java)
                intent.putExtra("dialog_type", "offer")
                //finish()
                startActivity(intent)
            }
            R.id.profile -> {
                val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                //finish()
                startActivity(intent)
            }
            R.id.logout -> {
                val intent = Intent(this@HomeActivity, RideDialogActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}