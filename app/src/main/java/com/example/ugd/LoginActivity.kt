package com.example.ugd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    lateinit var mBundle : Bundle

     var lUsername: String = ""
     var lPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //getBundle()

        setTitle("User Login")

        inputEmail = findViewById(R.id.inputLayoutEmail)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnSignUp = findViewById<Button>(R.id.btnSign)
        val btnLogin : Button = findViewById(R.id.btnLogin)

        //aksi btnRegister/btn sign up
        btnSignUp.setOnClickListener{
            val moveReg = Intent(this, RegisterActivity::class.java)
            startActivity(moveReg)
        }

        //aksi pada btn login
        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val username: String = inputEmail.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            //pengecekan apakah inputan kosong
            if (username.isEmpty()) {
                inputEmail.setError("Email Must be Filled With Text")
                checkLogin = false
            }
            //pengecekan apakah inputan kosong
            if (password.isEmpty()) {
                inputPassword.setError("Password Must be Filled With Text")
                checkLogin = false
            }

            if ((username == lUsername && password == lPassword) || (username != "" && password != "")) {
                Log.d("tes","check login true")
                checkLogin = true

            }

            if (!checkLogin){
                Log.d("tes","check login false")
                return@OnClickListener
            } else {
                val moveHome = Intent(this@LoginActivity, Home::class.java)
                startActivity(moveHome)
            }
        })
    }

    fun getBundle(){
        mBundle = intent.getBundleExtra("register")!!
        lUsername = mBundle.getString("username")!!
        lPassword = mBundle.getString("password")!!
    }
}