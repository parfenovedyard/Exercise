package com.example.exercises.activity


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

class DataBaseActivity : BaseActivity() {

    private lateinit var binding: ActivityDataBaseBinding
    private lateinit var adapter: UserDbAdapter
    private lateinit var db: SQLiteDatabase
    private var usersPhoneBook: ArrayList<PhoneBookUser> = ArrayList()
    private var photoFromCamera: Uri? = null

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
                choosePhotoFromGallery()
            })
        recyclerView.adapter = adapter

        setupActionBar(binding.tbDb)

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
                usersPhoneBook[user.id - 1] = PhoneBookUser(user.id, firstName, lastName,
                    phone, user.image)
                adapter.phoneBook = usersPhoneBook
                adapter.notifyDataSetChanged()

                val values = ContentValues().apply {
                    put(DataBaseHelper.FIRST_NAME, firstName)
                    put(DataBaseHelper.LAST_NAME, lastName)
                    put(DataBaseHelper.PHONE, phone)
                }

                db.update(
                    DataBaseHelper.TABLE_NAME,
                    values,
                    "${DataBaseHelper.COLUMN_ID} = ?",
                    arrayOf(user.id.toString())
                )

                dialog.dismiss()
               // db.close()
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

    private fun deleteUser(user: PhoneBookUser) {
        db.delete(
            DataBaseHelper.TABLE_NAME,
            "${DataBaseHelper.COLUMN_ID} = ?",
            arrayOf(user.id.toString())
        )
        db.close()
        Log.e("ups", usersPhoneBook[0].firstName)

        //usersPhoneBook.removeAt(user.id - 1)
       // usersPhoneBook = getDataFromDb()
        usersPhoneBook.map { getDataFromDb() }.toTypedArray()
        Log.e("ups", usersPhoneBook[0].firstName)


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
                        startActivityForResult(intent, GALLERY)
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
                photoFromCamera = data!!.data

                Glide.with(this)
                    .load(photoFromCamera)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person_24)
                    .into(findViewById(R.id.iv_user_image))
            }
        }
    }

    companion object{
        private const val GALLERY = 1
    }

}