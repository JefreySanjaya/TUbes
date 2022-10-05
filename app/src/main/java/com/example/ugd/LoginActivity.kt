package com.example.ugd

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.ugd.databinding.ActivityLoginBinding
import com.example.ugd.room.NoteDB
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {
    val db by lazy { NoteDB(this) }
    private lateinit var binding: ActivityLoginBinding
    lateinit var  mBundle: Bundle

    private val key = "nameKey"
    private val id = "idKey"
    private val myPreference = "login"
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login)

        getSupportActionBar()?.hide()
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)

        val viewBinding = binding.root
        val moveHome = Intent(this@LoginActivity, Home::class.java)

        if(!sharedPreferences!!.contains(key)){
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putString(key, "terisi")
            editor.apply()
            setContentView(R.layout.activity_splash_screen)

            Handler(Looper.getMainLooper()).postDelayed({
                setContentView(viewBinding)
            }, 3000)
        }else{
            setContentView(viewBinding)
        }

        if (intent.hasExtra("register")) {
            mBundle = intent.getBundleExtra("register")!!
            inputUsername.setText(mBundle.getString("username"))
            inputPassword.setText(mBundle.getString("password"))
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false

            CoroutineScope(Dispatchers.IO).launch {
                val users = db.userDao().getUser()
                Log.d("LoginActivity ","dbResponse: $users")

                for(i in users){
                    if(inputUsername.text.toString() == i.username && inputPassword.text.toString() == i.password){
                        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                        editor.putString("id", i.id.toString())
                        editor.apply()
                        checkLogin=true
                        break
                    }
                }

                withContext(Dispatchers.Main){
                    if((inputUsername.text.toString() == "admin" && inputPassword.text.toString() == "admin") || (checkLogin)){
                        checkLogin = false
                        startActivity(moveHome)
                        finish()
                    }else {
                        if (inputLayoutUsername.getEditText()?.getText().toString().isEmpty()) {
                            inputLayoutUsername.setError("Username must be filled with Text")
                        }else if (inputLayoutUsername.getEditText()?.getText().toString() != "admin") {
                            inputLayoutUsername.setError("Username false")
                        }

                        if (inputLayoutPassword.getEditText()?.getText().toString().isEmpty()) {
                            inputLayoutPassword.setError("Password must ben filled with text")
                        }else if (inputLayoutPassword.getEditText()?.getText().toString() != "admin") {
                            inputLayoutPassword.setError("Password false")
                        }
                    }
                }
            }
        })

        btnSignUp.setOnClickListener {
            val moveRegis = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveRegis)
        }
    }

}