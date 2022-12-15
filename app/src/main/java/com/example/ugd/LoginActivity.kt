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
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd.api.PesananApi
import com.example.ugd.databinding.ActivityLoginBinding
import com.example.ugd.models.UserLogin
import com.example.ugd.room.NoteDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import kotlin.math.log


class LoginActivity : AppCompatActivity() {
    val db by lazy { NoteDB(this) }
    private lateinit var binding: ActivityLoginBinding
    lateinit var  mBundle: Bundle

    private val key = "nameKey"
    private val id = "idKey"
    private val myPreference = "login"
    private var queue: RequestQueue? = null
    var sharedPreferences: SharedPreferences? = null
    var SP: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login)
        queue = Volley.newRequestQueue(this)
        getSupportActionBar()?.hide()
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        SP = getSharedPreferences("UserId", Context.MODE_PRIVATE)

        val viewBinding = binding.root



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


//                    if(inputUsername.text.toString() == i.username && inputPassword.text.toString() == i.password){
//                        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
//                        editor.putString("id", i.id.toString())
//                        editor.apply()
                        checkLogin=true
//                        break
//                    }

                withContext(Dispatchers.Main){
                    if((checkLogin)){
                        login()
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



    private fun login() {


        val userLogin = UserLogin(
            binding.inputUsername.text.toString(),
            binding.inputPassword.text.toString()
        )

        val user: StringRequest =
            object : StringRequest(Request.Method.POST, PesananApi.login, Response.Listener { response ->
                val gson = Gson()
                var login = gson.fromJson(response, UserLogin::class.java)
                val jsonObject = JSONObject(response)
                if (login != null){
                    Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                }
                val moveHome = Intent(this@LoginActivity, Home::class.java)
                val userID : SharedPreferences.Editor = SP!!.edit()
                userID.putInt("id", jsonObject.getJSONObject("user").getInt("id"))
                userID.apply()
                startActivity(moveHome)
                finish()
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@LoginActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(userLogin)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(user)
    }


}