package com.project.countplusplus.database

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.project.countplusplus.models.PaymentModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PaymentDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "countplusplus"

        private val TABLE_CONTACTS = "payments"

        private val KEY_ID = "_id"
        private val KEY_SENDER_NAME = "sender_name"
        private val KEY_SENDER_EMAIL = "sender_email"
        private val KEY_RECIVER_ID = "reciver_id"
        private val KEY_AMOUNT = "amount"
        private val KEY_DUE_DATE = "due_date"
        private val KEY_RECIEVED = "recieved"
        private val KEY_RECIVED_DATE = "recvied_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SENDER_NAME + " TEXT,"
                + KEY_SENDER_EMAIL + " TEXT,"
                + KEY_RECIVER_ID + " INTEGER,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_DUE_DATE + " TEXT,"
                + KEY_RECIEVED + " TEXT,"
                + KEY_RECIVED_DATE + " TEXT"
                + ")")
//        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addPayment(pm: PaymentModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_SENDER_NAME, pm.sender_name)
        contentValues.put(KEY_SENDER_EMAIL, pm.sender_email)
        contentValues.put(KEY_RECIVER_ID, pm.reciver_id)
        contentValues.put(KEY_AMOUNT, pm.amount)
        contentValues.put(KEY_DUE_DATE, pm.due_date)
        contentValues.put(KEY_RECIEVED, pm.recieved)
        contentValues.put(KEY_RECIVED_DATE, pm.recvied_date)

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }


    fun viewPayments(myId: Int): ArrayList<PaymentModel> {

        val paymentList: ArrayList<PaymentModel> = ArrayList<PaymentModel>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS WHERE reciver_id = $myId"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var sender_name: String
        var sender_email: String
        var reciver_id: Int
        var amount: String
        var due_date: String
        var recieved: String
        var recvied_date: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                sender_name = cursor.getString(cursor.getColumnIndex(KEY_SENDER_NAME))
                sender_email = cursor.getString(cursor.getColumnIndex(KEY_SENDER_EMAIL))
                reciver_id = cursor.getInt(cursor.getColumnIndex(KEY_RECIVER_ID))
                amount = cursor.getString(cursor.getColumnIndex(KEY_AMOUNT))
                due_date = cursor.getString(cursor.getColumnIndex(KEY_DUE_DATE))
                recieved = cursor.getString(cursor.getColumnIndex(KEY_RECIEVED))
                recvied_date = cursor.getString(cursor.getColumnIndex(KEY_RECIVED_DATE))

                val pm = PaymentModel(id,sender_name,sender_email,reciver_id,amount,due_date,recieved,recvied_date)
                paymentList.add(pm)

            } while (cursor.moveToNext())
        }
        return paymentList
//        return ArrayList()
    }

    fun notifyForPayments(myId: Int): ArrayList<PaymentModel>{
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val arr: List<String> = currentDate.split(" ")
        val date = arr[0]
        Log.d("reciver_id", myId.toString())
        Log.d("today date", date)

        val paymentList: ArrayList<PaymentModel> = ArrayList<PaymentModel>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS WHERE reciver_id = $myId AND due_date= '$date'"
        Log.d("Query", selectQuery)

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var sender_name: String
        var sender_email: String
        var reciver_id: Int
        var amount: String
        var due_date: String
        var recieved: String
        var recvied_date: String

        if (cursor.moveToFirst()) {
            Log.d("record exist", "yes")
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                sender_name = cursor.getString(cursor.getColumnIndex(KEY_SENDER_NAME))
                sender_email = cursor.getString(cursor.getColumnIndex(KEY_SENDER_EMAIL))
                reciver_id = cursor.getInt(cursor.getColumnIndex(KEY_RECIVER_ID))
                amount = cursor.getString(cursor.getColumnIndex(KEY_AMOUNT))
                due_date = cursor.getString(cursor.getColumnIndex(KEY_DUE_DATE))
                recieved = cursor.getString(cursor.getColumnIndex(KEY_RECIEVED))
                recvied_date = cursor.getString(cursor.getColumnIndex(KEY_RECIVED_DATE))

                val pm = PaymentModel(id,sender_name,sender_email,reciver_id,amount,due_date,recieved,recvied_date)
                paymentList.add(pm)

            } while (cursor.moveToNext())
        }
        else
        {
            Log.d("record exist", "no")
        }
        return paymentList
//        return ArrayList()
    }

    fun deletePayment(paymentId: Int){
        val db = this.writableDatabase
        var res = db.delete(TABLE_CONTACTS,"_id=?", arrayOf(paymentId.toString()))
        db.close()
    }

    fun updateData(pm: PaymentModel, paymentId: Int)  {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_SENDER_NAME, pm.sender_name)
        contentValues.put(KEY_SENDER_EMAIL, pm.sender_email)
        contentValues.put(KEY_RECIVER_ID, pm.reciver_id)
        contentValues.put(KEY_AMOUNT, pm.amount)
        contentValues.put(KEY_DUE_DATE, pm.due_date)
        contentValues.put(KEY_RECIEVED, pm.recieved)
        contentValues.put(KEY_RECIVED_DATE, pm.recvied_date)

        val count = db.update(TABLE_CONTACTS, contentValues, "_id=?", arrayOf(paymentId.toString()))

        db.close()
    }
}