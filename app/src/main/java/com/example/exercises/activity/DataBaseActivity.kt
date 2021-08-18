package com.example.exercises.activity


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exercises.DataBaseHelper
import com.example.exercises.adapters.UserDbAdapter
import com.example.exercises.databinding.ActivityDataBaseBinding
import com.example.exercises.databinding.DialogEditBinding
import com.example.exercises.models.PhoneBookUser

class DataBaseActivity : BaseActivity() {

    private lateinit var binding: ActivityDataBaseBinding
    private lateinit var adapter: UserDbAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usersPhoneBook = getDataFromDb()

        val recyclerView: RecyclerView = binding.rvDataBase
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = UserDbAdapter(usersPhoneBook,
            {
                dialogEditUser(it)
            },{
                Log.e("ups", it.lastName)
            })
        recyclerView.adapter = adapter

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
                    cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_ID)),
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

    private fun dialogEditUser(user: PhoneBookUser) {
        val binding = DialogEditBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(binding.root)

        binding.etFirstName.setText(user.firstName)
        binding.etLastName.setText(user.lastName)
        binding.etPhone.setText(user.phone)

        val phoneBook = getDataFromDb()

        binding.tvBtnEdit.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val phone = binding.etPhone.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty()) {
                phoneBook[user.id - 1] = PhoneBookUser(user.id, firstName, lastName, phone)
                adapter.phoneBook = phoneBook
                adapter.notifyDataSetChanged()

                Log.e("ups", firstName + user.id)

                dialog.dismiss()
            }
        }
        binding.tvBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}