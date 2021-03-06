package com.example.exercises

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,
    null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY," +
                " $FIRST_NAME TEXT, $LAST_NAME TEXT, $PHONE TEXT, $IMAGE TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db?.execSQL("CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY," +
                " $FIRST_NAME TEXT, $LAST_NAME TEXT, $PHONE TEXT, $IMAGE TEXT)")
    }

    companion object{
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "PhoneBook.db"

        const val TABLE_NAME = "phonebook"
        const val COLUMN_ID = "_id"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val IMAGE = "image"
        const val PHONE = "phone"
    }
}