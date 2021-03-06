package com.example.exercises.activity


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exercises.DataBaseHelper
import com.example.exercises.R
import com.example.exercises.adapters.UserDbAdapter
import com.example.exercises.databinding.ActivityDataBaseBinding
import com.example.exercises.databinding.DialogEditBinding
import com.example.exercises.models.PhoneBookUser
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*
import kotlin.collections.ArrayList

class DataBaseActivity : BaseActivity() {

    private lateinit var binding: ActivityDataBaseBinding
    private lateinit var adapter: UserDbAdapter
    private lateinit var db: SQLiteDatabase
    private lateinit var mUser: PhoneBookUser
    private var usersPhoneBook: ArrayList<PhoneBookUser> = ArrayList()
    private var KEY_COUNT = "COUNT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usersPhoneBook = getDataFromDb()

        val recyclerView: RecyclerView = binding.rvDataBase
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = UserDbAdapter(usersPhoneBook,
            {
                dialogEditUser(it)
            },{
                deleteUser(it)
            },{
                mUser = it
                choosePhotoFromGallery()
            })
        recyclerView.adapter = adapter

        setupActionBar(binding.tbDb)

        binding.addUser.setOnClickListener {
            addUser()
        }

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

    }

    private fun getDataFromDb(): ArrayList<PhoneBookUser> {
        val list = ArrayList<PhoneBookUser>()
        val dbHelper = DataBaseHelper(this)
        db = dbHelper.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM ${DataBaseHelper.TABLE_NAME}", null)
        while (cursor.moveToNext()) {
            list.add(
                PhoneBookUser(
                    cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.PHONE)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.IMAGE))
                )
            )
        }
        cursor.close()
        //db.close()
        return list
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun dialogEditUser(user: PhoneBookUser) {
        val binding = DialogEditBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(binding.root)

        binding.etFirstName.setText(user.firstName)
        binding.etLastName.setText(user.lastName)
        binding.etPhone.setText(user.phone)

        binding.tvBtnEdit.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val phone = binding.etPhone.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty()) {

                val index = usersPhoneBook.indexOf(user)
                usersPhoneBook[index] = PhoneBookUser(user.id, firstName, lastName,
                    phone, user.image)

                usersPhoneBook.sortWith(
                    compareBy(
                        { it.firstName.lowercase(Locale.getDefault())},
                        { it.lastName.lowercase(Locale.getDefault())},
                        { it.phone.lowercase(Locale.getDefault())}
                    )
                )

                db.execSQL("DELETE FROM ${DataBaseHelper.TABLE_NAME}")
                val values = ContentValues()
                for (i in 0 until usersPhoneBook.size){
                    //values.put(DataBaseHelper.COLUMN_ID, usersPhoneBook[i].id)
                    values.put(DataBaseHelper.FIRST_NAME, usersPhoneBook[i].firstName)
                    values.put(DataBaseHelper.LAST_NAME, usersPhoneBook[i].lastName)
                    values.put(DataBaseHelper.PHONE, usersPhoneBook[i].phone)
                    values.put(DataBaseHelper.IMAGE, usersPhoneBook[i].image)
                    db.insert(DataBaseHelper.TABLE_NAME, null, values)
                }

                adapter.phoneBook = usersPhoneBook
                adapter.notifyDataSetChanged()

                dialog.dismiss()
            }else{
                Toast.makeText(this,"Please fill in all fields",
                    Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteUser(user: PhoneBookUser) {

        usersPhoneBook.remove(user)

        db.execSQL("DELETE FROM ${DataBaseHelper.TABLE_NAME}")
        val values = ContentValues()
        for (i in 0 until usersPhoneBook.size){
            values.put(DataBaseHelper.FIRST_NAME, usersPhoneBook[i].firstName)
            values.put(DataBaseHelper.LAST_NAME, usersPhoneBook[i].lastName)
            values.put(DataBaseHelper.PHONE, usersPhoneBook[i].phone)
            values.put(DataBaseHelper.IMAGE, usersPhoneBook[i].image)
            db.insert(DataBaseHelper.TABLE_NAME, null, values)
        }

        adapter.phoneBook = usersPhoneBook
        adapter.notifyDataSetChanged()

        Toast.makeText(this,"Deleted",
            Toast.LENGTH_SHORT).show()
    }

    private fun choosePhotoFromGallery(){
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ).withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if(p0!!.areAllPermissionsGranted()){
                        val intent = Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(intent, GALLERY,)
                    }
                    if (p0.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(this@DataBaseActivity,"No permission",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }

            }).onSameThread().check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
          if (requestCode == GALLERY) {
              setUserImage(data!!.data)
              Log.e("ups", "data from ActivityForResult: ${data.data.toString()}")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUserImage(data: Uri?) {

        val index = usersPhoneBook.indexOf(mUser)

        Log.e("ups", mUser.firstName + data.toString())

        val values = ContentValues().apply {
            put(DataBaseHelper.IMAGE, data.toString())
        }

        db.update(
            DataBaseHelper.TABLE_NAME,
            values,
            "${DataBaseHelper.COLUMN_ID} = ?",
            arrayOf(mUser.id.toString())
        )
        usersPhoneBook[index].image = data.toString()
        adapter.phoneBook = usersPhoneBook
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addUser() {
        val binding = DialogEditBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(binding.root)

        binding.tvBtnEdit.text = resources.getString(R.string.ADD)
        var newId = getDataFromDb().maxByOrNull { it.id }?.id
        newId = newId!! + 1

        binding.tvBtnEdit.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val phone = binding.etPhone.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty()) {

                val newUser = PhoneBookUser(newId, firstName, lastName, phone, "")
                usersPhoneBook = getDataFromDb()
                usersPhoneBook.add(newUser)
                usersPhoneBook.sortWith(
                    compareBy(
                        { it.firstName.lowercase(Locale.getDefault())},
                        { it.lastName.lowercase(Locale.getDefault())},
                        { it.phone.lowercase(Locale.getDefault())}
                    )
                )
                Log.e("ups", usersPhoneBook[0].toString())

                db.execSQL("DELETE FROM ${DataBaseHelper.TABLE_NAME}")
                val values = ContentValues()
                for (i in 0 until usersPhoneBook.size){
                    //values.put(DataBaseHelper.COLUMN_ID, usersPhoneBook[i].id)
                    values.put(DataBaseHelper.FIRST_NAME, usersPhoneBook[i].firstName)
                    values.put(DataBaseHelper.LAST_NAME, usersPhoneBook[i].lastName)
                    values.put(DataBaseHelper.PHONE, usersPhoneBook[i].phone)
                    values.put(DataBaseHelper.IMAGE, usersPhoneBook[i].image)
                    db.insert(DataBaseHelper.TABLE_NAME, null, values)
                }

                adapter.phoneBook = usersPhoneBook
                adapter.notifyDataSetChanged()

                dialog.dismiss()
            }else{
                Toast.makeText(this,"Please fill in all fields",
                    Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    companion object{
        private const val GALLERY = 1
    }

}