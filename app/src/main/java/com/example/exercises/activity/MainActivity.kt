package com.example.exercises.activity


import android.content.Intent
import android.os.Bundle
import com.example.exercises.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGeneratePhoneBook.setOnClickListener {
            startActivity(Intent(this, PhoneBookActivity::class.java))
        }

        binding.btnPhoneBookFromBd.setOnClickListener {
            startActivity(Intent(this, DataBaseActivity::class.java))
        }
        setupActionBar(binding.tbMain)
    }


}