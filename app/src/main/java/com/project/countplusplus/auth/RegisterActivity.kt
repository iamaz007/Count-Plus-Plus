package com.project.countplusplus.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.countplusplus.R
import com.project.countplusplus.database.UsersDatabaseHandler
import com.project.countplusplus.models.UserModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("users")

        register()
    }

    private fun register() {
        var regBtn = findViewById<Button>(R.id.registerBtn)

        regBtn.setOnClickListener {

            if(TextUtils.isEmpty(registerName.text.toString())) {
                registerName.setError("Please enter first name ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(registerPhoneNo.text.toString())) {
                registerPhoneNo.setError("Please enter Phone No")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(registerEmail.text.toString())) {
                registerEmail.setError("Please enter Email")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(registerPassword.text.toString())) {
                registerPassword.setError("Please enter password")
                return@setOnClickListener
            }

//          save user in firebase

            auth.createUserWithEmailAndPassword(registerEmail.text.toString(), registerPassword.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
                        currentUSerDb?.child("fullName")?.setValue(registerName.text.toString())
                        currentUSerDb?.child("phoneNo")?.setValue(registerPhoneNo.text.toString())

                        Toast.makeText(this@RegisterActivity, "Registration Success. ", Toast.LENGTH_LONG).show()
                        finish()

                    } else {
                        Log.w("createUserWithEmail", it.exception)
                        Toast.makeText(this@RegisterActivity, "Registration failed, please try again! ", Toast.LENGTH_LONG).show()
                    }
                }
//            save user email in sqlite
            val databaseHandler: UsersDatabaseHandler = UsersDatabaseHandler(this)
            if (registerEmail.text.toString().isNotEmpty()) {
                val status =
                    databaseHandler.addUser(UserModel(0, registerEmail.text.toString()))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        loginIntent.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))

        }
    }
}