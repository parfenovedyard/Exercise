package com.example.exercises.activity


import androidx.appcompat.app.AppCompatActivity
import com.example.exercises.R

open class BaseActivity: AppCompatActivity() {

     fun setupActionBar(toolbar: androidx.appcompat.widget.Toolbar) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}