package com.project.countplusplus.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.project.countplusplus.models.UserModel

class UsersDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "countplusplus"

        private val TABLE_CONTACTS = "users"

        private val KEY_ID = "_id"
        private val KEY_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EMAIL + " TEXT" + ")")
//        db?.execSQL(CREATE_CONTACTS_TABLE)
        db?.execSQL("CREATE TABLE users (_id INTEGER PRIMARY KEY, email TEXT)")
        db?.execSQL("CREATE TABLE payments (_id INTEGER PRIMARY KEY, sender_name TEXT, sender_email TEXT, reciver_id TEXT, amount INTEGER, due_date TEXT, recieved TEXT, recvied_date TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addUser(user: UserModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_EMAIL, user.email) // EmpModelClass Email

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    fun getUserId(email:String): Int {
        val db = this.writableDatabase
        val selectQuery = "SELECT * FROM users WHERE email = ?"

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, arrayOf(email))
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
        }
        var id= 0
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(UsersDatabaseHandler.KEY_ID))
            }
        }
        return id
    }
}