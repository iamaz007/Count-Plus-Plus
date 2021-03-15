package com.project.countplusplus.payment

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.countplusplus.R
import com.project.countplusplus.database.PaymentDatabaseHandler
import com.project.countplusplus.database.UsersDatabaseHandler
import com.project.countplusplus.models.PaymentModel
import kotlinx.android.synthetic.main.activity_add_payment.*
import kotlinx.android.synthetic.main.activity_add_payment.goBack
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class AddPaymentActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null
    var myEmail=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("users")

        loadDatePicker()
        goBackFunc()
        getNewPayment()
    }

    private fun getNewPayment() {
        //getting it from xml
        var editTextPersonName = findViewById(R.id.editTextPersonName) as EditText
        var addPaymentEmail = findViewById(R.id.addPaymentEmail) as EditText
        var addPaymentAmount = findViewById(R.id.addPaymentAmount) as EditText
        var addPaymentDate = findViewById(R.id.addPaymentDate) as EditText

        //adding a click listener to button
        addPaymentBtn.setOnClickListener { addPayment(
            editTextPersonName.text.toString(),
            addPaymentEmail.text.toString(),
            addPaymentAmount.text.toString(),
            addPaymentDate.text.toString()
        ) }
    }

    private fun addPayment(
        editTextPersonName: String,
        addPaymentEmail: String,
        addPaymentAmount: String,
        addPaymentDate: String
    ) {
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)
        if (user != null) {
            myEmail = user.email.toString()
        }

        val databaseHandler: PaymentDatabaseHandler = PaymentDatabaseHandler(this)
        val userDatabaseHandler: UsersDatabaseHandler = UsersDatabaseHandler(this)
        val myId = userDatabaseHandler.getUserId(myEmail)

        if (editTextPersonName.isNotEmpty() && addPaymentEmail.isNotEmpty() && addPaymentAmount.isNotEmpty() && addPaymentDate.isNotEmpty()) {
            val status =
                databaseHandler.addPayment(PaymentModel(
                    0,
                    editTextPersonName,
                    addPaymentEmail,
                    myId,
                    addPaymentAmount,
                    addPaymentDate,
                    "",
                    ""
                ))
            if (status > -1) {
                Toast.makeText(applicationContext, "Payment saved", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Fields cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }

    }


    private fun loadDatePicker() {
        var picker: DatePickerDialog
        var addPaymentDate = findViewById<View>(R.id.addPaymentDate) as EditText
        addPaymentDate.setInputType(InputType.TYPE_NULL)
        addPaymentDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val cldr: Calendar = Calendar.getInstance()
                val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
                val month: Int = cldr.get(Calendar.MONTH)
                val year: Int = cldr.get(Calendar.YEAR)
                // date picker dialog
                picker = DatePickerDialog(
                    this@AddPaymentActivity,
                    { view, year, monthOfYear, dayOfMonth -> addPaymentDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                    year,
                    month,
                    day
                )
                picker.show()
            }
        })
    }

    private fun goBackFunc() {
        goBack.setOnClickListener{
            this.finish()
        }
    }
}