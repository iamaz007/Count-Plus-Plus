package com.project.countplusplus.payment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.countplusplus.R
import com.project.countplusplus.database.PaymentDatabaseHandler
import com.project.countplusplus.database.UsersDatabaseHandler
import com.project.countplusplus.models.PaymentModel
import kotlinx.android.synthetic.main.activity_add_payment.*
import kotlinx.android.synthetic.main.activity_edit_payment.*
import java.util.*


class EditPaymentActivity : AppCompatActivity() {
    var paymentId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_payment)

        paymentId = intent.getIntExtra("paymentId", 0)

        loadDatePicker()
        populateFields()
        goBackFunc()
        loadStatusDropDown()
        deletePayment()
    }

    fun populateFields()
    {
        var sender_name = intent.getIntExtra("sender_name", 0)
        var sender_email= intent.getIntExtra("sender_email", 0)
        var reciver_id: Int
        var amount= intent.getIntExtra("amount", 0)
        var due_date= intent.getIntExtra("due_date", 0)
        var recieved: String
        var recvied_date: String
        editSenderName.setText(sender_name)
        editSenderEmail.setText(sender_email)
        editAmount.setText(amount)
        editDueDate.setText(due_date)

    }

    private fun goBackFunc() {
        goBackFromEditPayment.setOnClickListener{
            this.finish()
        }
    }

    private fun loadDatePicker() {
        var picker: DatePickerDialog
        var addPaymentDate = findViewById<View>(R.id.editDueDate) as EditText
        addPaymentDate.setInputType(InputType.TYPE_NULL)
        addPaymentDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val cldr: Calendar = Calendar.getInstance()
                val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
                val month: Int = cldr.get(Calendar.MONTH)
                val year: Int = cldr.get(Calendar.YEAR)
                // date picker dialog
                picker = DatePickerDialog(
                        this@EditPaymentActivity,
                        { view, year, monthOfYear, dayOfMonth -> editDueDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                        year,
                        month,
                        day
                )
                picker.show()
            }
        })
    }

    private fun loadStatusDropDown() {
        val dropdown = findViewById<Spinner>(R.id.editPaymentStatus)
        val items = arrayOf("No", "Yes")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter
    }


    fun deletePayment() {
        button3.setOnClickListener{
            val databaseHandler: PaymentDatabaseHandler = PaymentDatabaseHandler(this)
            Log.d("paymentId",paymentId.toString())
            //calling the viewEmployee method of DatabaseHandler class to read the records
            val res = databaseHandler.deletePayment(paymentId)

            // Create an intent
            val replyIntent = Intent()
            // Put the data to return into the extra
            replyIntent.putExtra("ROW_DELETED", 1)
            // Set the activity's result to RESULT_OK
            setResult(RESULT_OK, replyIntent)

            this.finish()
        }
    }

    private fun updatePayment(
            editSenderName: String,
            editSenderEmail: String,
            editAmount: String,
            editDueDate: String
    ) {
//        val user = auth.currentUser
//        val userreference = databaseReference?.child(user?.uid!!)
//        if (user != null) {
//            myEmail = user.email.toString()
//        }
//
//        val databaseHandler: PaymentDatabaseHandler = PaymentDatabaseHandler(this)
//        val userDatabaseHandler: UsersDatabaseHandler = UsersDatabaseHandler(this)
//        val myId = userDatabaseHandler.getUserId(myEmail)
//
//        if (editTextPersonName.isNotEmpty() && addPaymentEmail.isNotEmpty() && addPaymentAmount.isNotEmpty() && addPaymentDate.isNotEmpty()) {
//            val status =
//                    databaseHandler.addPayment(PaymentModel(
//                            0,
//                            editTextPersonName,
//                            addPaymentEmail,
//                            myId,
//                            addPaymentAmount,
//                            addPaymentDate,
//                            "",
//                            ""
//                    ))
//            if (status > -1) {
//                Toast.makeText(applicationContext, "Payment saved", Toast.LENGTH_LONG).show()
//            }
//        } else {
//            Toast.makeText(
//                    applicationContext,
//                    "Fields cannot be blank",
//                    Toast.LENGTH_LONG
//            ).show()
//        }

    }
}