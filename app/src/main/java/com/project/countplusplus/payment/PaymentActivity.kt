package com.project.countplusplus.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.project.countplusplus.R
import com.project.countplusplus.adapters.PaymentRecyclerAdapter
import com.project.countplusplus.database.PaymentDatabaseHandler
import com.project.countplusplus.database.UsersDatabaseHandler
import com.project.countplusplus.models.PaymentModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var myEmail=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        auth = FirebaseAuth.getInstance()
        setupListofDataIntoRecyclerView()
        goBackFunc()
        refreshPaymentFunc()
        addPaymentActivity()


    }

    private fun refreshPaymentFunc() {
        refreshData.setOnClickListener {
            setupListofDataIntoRecyclerView()
        }
    }

    private fun setupListofDataIntoRecyclerView() {

        if (getPayments().size > 0) {

            recyclerView.visibility = View.VISIBLE
            textView8.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            recyclerView.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = PaymentRecyclerAdapter(this, getPayments())
            // adapter instance is set to the recyclerview to inflate the items.
            recyclerView.adapter = itemAdapter
        } else {

            recyclerView.visibility = View.GONE
            textView8.visibility = View.VISIBLE
        }
    }

    private fun getPayments(): ArrayList<PaymentModel> {
        val user = auth.currentUser
        if (user != null) {
            myEmail = user.email.toString()
        }
        val userDatabaseHandler: UsersDatabaseHandler = UsersDatabaseHandler(this)
        val myId = userDatabaseHandler.getUserId(myEmail)
        //creating the instance of DatabaseHandler class
        val databaseHandler: PaymentDatabaseHandler = PaymentDatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val empList: ArrayList<PaymentModel> = databaseHandler.viewPayments(myId)

        return empList
    }

    private fun addPaymentActivity() {
        addPaymentActivity.setOnClickListener {
            startActivity(Intent(this@PaymentActivity, AddPaymentActivity::class.java))
        }
        setupListofDataIntoRecyclerView()
    }

    private fun goBackFunc() {
        goBack.setOnClickListener{
            this.finish()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        refreshPaymentFunc()
    }
}