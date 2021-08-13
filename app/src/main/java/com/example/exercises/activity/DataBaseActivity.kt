package com.example.exercises.activity


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exercises.DataBaseHelper
import com.example.exercises.adapters.UserDbAdapter
import com.example.exercises.databinding.ActivityDataBaseBinding
import com.example.exercises.models.PhoneBookUser

class DataBaseActivity : BaseActivity() {

    private lateinit var binding: ActivityDataBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataFromDb()

        val usersPhoneBook = getDataFromDb()

        val recyclerView: RecyclerView = binding.rvDataBase
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserDbAdapter(usersPhoneBook)

        setupActionBar(binding.tbDb)
    }

    private fun getDataFromDb(): ArrayList<PhoneBookUser> {
        val list = ArrayList<PhoneBookUser>()
        val dbHelper = DataBaseHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM ${DataBaseHelper.TABLE_NAME}", null)
        while (cursor.moveToNext()) {
            list.add(
                PhoneBookUser(
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.PHONE))
                )
            )
        }
        cursor.close()
        db.close()
        return list
    }
}