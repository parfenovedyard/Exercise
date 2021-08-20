package com.example.exercises.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exercises.*
import com.example.exercises.adapters.UserAdapter
import com.example.exercises.databinding.ActivityPhoneBookBinding
import com.example.exercises.models.PhoneBookUser


class PhoneBookActivity : BaseActivity() {

    private lateinit var binding: ActivityPhoneBookBinding
    private lateinit var firstNames: List<String>
    private lateinit var lastNames: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstNames = resources.openRawResource(R.raw.names).reader().readText()
            .split(", ")
            .map { it.trim('"') }

        lastNames = resources.openRawResource(R.raw.surnames).reader().readText()
            .split(", ")
            .map { it.trim('"') }

        val usersPhoneBook = getRandomPhoneArray()

        val recyclerView: RecyclerView = binding.rvRandom
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserAdapter(usersPhoneBook) {
            Toast.makeText(this," Unavailable, Please save PhoneBook to DataBase",
                Toast.LENGTH_LONG).show()
        }

        binding.btnSaveToBd.setOnClickListener {
           addPhoneBookToDB(usersPhoneBook)
           startActivity(Intent(this, DataBaseActivity::class.java))
       }

        setupActionBar(binding.tbPhoneBook)

    }

    private fun getRandomFirstNameFromTxt(firstNames: List<String>): String {
        return firstNames[(0..firstNames.size).random()]
    }
    private fun getRandomLastNameFromTxt(lastNames: List<String>): String {
        return lastNames[(0..lastNames.size).random()]
    }
    private fun getRandomNumber(): String {
        var phoneNumber = "89"
        for(i in 1..9){
            phoneNumber += (0..9).random()
        }
        return phoneNumber
    }
    private fun getRandomPhoneArray(): ArrayList<PhoneBookUser> {
        val usersPhoneBook: ArrayList<PhoneBookUser> = arrayListOf()
        for (i in 1 .. 10) {     // тут задаем количесвто людей в книге
            usersPhoneBook.add(
                PhoneBookUser(i,getRandomFirstNameFromTxt(firstNames),
                getRandomLastNameFromTxt(lastNames), getRandomNumber(), "")
            )
        }
        usersPhoneBook.sortWith(
            compareBy(
                { it.firstName },
                { it.lastName },
                { it.phone }
            )
        )
        return usersPhoneBook
    }

    private fun addPhoneBookToDB(phoneBook: ArrayList<PhoneBookUser>){
        val dbHelper = DataBaseHelper(this)
        val db = dbHelper.writableDatabase

        db.execSQL("DELETE FROM ${DataBaseHelper.TABLE_NAME}")

        val values = ContentValues()
        for (i in 0 until phoneBook.size){
            values.put(DataBaseHelper.FIRST_NAME, phoneBook[i].firstName)
            values.put(DataBaseHelper.LAST_NAME, phoneBook[i].lastName)
            values.put(DataBaseHelper.PHONE, phoneBook[i].phone)
            values.put(DataBaseHelper.IMAGE, phoneBook[i].image)
            db.insert(DataBaseHelper.TABLE_NAME, null, values)
        }
        db.close()
    }

}