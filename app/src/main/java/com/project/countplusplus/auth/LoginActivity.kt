package com.project.countplusplus.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.countplusplus.R
import com.project.countplusplus.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val currentuser = auth.currentUser
        if(currentuser != null) {
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
        }
        login()
    }

    private fun login() {

        loginBtn.setOnClickListener {

            if(TextUtils.isEmpty(loginEmail.text.toString())){
                loginEmail.setError("Please enter username")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(loginPassword.text.toString())){
                loginPassword.setError("Please enter password")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString())
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login failed, please try again! ", Toast.LENGTH_LONG).show()
                        }
                    }

        }

        registerIntent.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }
    }
}