package com.example.ugd

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.LoginFilter
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var btnSignUp : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        username = findViewById(R.id.inputUsername)
        password = findViewById(R.id.inputPassword)

        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("username", username.text.toString())
            mBundle.putString("password", password.text.toString())
            intent.putExtra("register", mBundle)

            startActivity(intent)
        }
    }
}