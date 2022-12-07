package com.example.ugd

import android.content.res.Configuration
import android.text.LoginFilter
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

import android.app.NotificationChannel
import com.example.ugd.room.NoteDB
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd.api.PesananApi
import com.example.ugd.databinding.ActivityRegisterBinding
import com.example.ugd.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

class RegisterActivity : AppCompatActivity() {
    val db by lazy { NoteDB(this) }
    private lateinit var binding: ActivityRegisterBinding
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        queue = Volley.newRequestQueue(this)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val viewBinding = binding.root
        setContentView(viewBinding)

        binding.btnSignUp.setOnClickListener {
            var checkRegis = false


            if (binding.inputUsername.text.toString().isEmpty() && binding.inputPassword.text.toString().isEmpty() && binding.inputEmail.text.toString().isEmpty() && binding.inputDate.text.toString().isEmpty() && binding.inputPhone.text.toString().isEmpty()) {
                if (binding.inputUsername.text.toString().isEmpty()) {
                    binding.inputUsername.setError("Username must be filled with Text")
                }
                if (binding.inputPassword.text.toString().isEmpty()) {
                    binding.inputPassword.setError("Password must be filled with Text")
                }
                if (binding.inputEmail.text.toString().isEmpty()) {
                    binding.inputEmail.setError("Email must be filled with Text")
                }
                if (binding.inputDate.text.toString().isEmpty()) {
                    binding.inputDate.setError("Date Birth must be filled with Text")
                }
                if (binding.inputPhone.text.toString().isEmpty()) {
                    binding.inputPhone.setError("Phone Number must be filled with Text")
                }
            } else {
                checkRegis = true
            }

            if (!checkRegis) return@setOnClickListener
            val mBundle = Bundle()

//            mBundle.putString("username", binding.inputUsername.text.toString())
//            mBundle.putString("password", binding.inputPassword.text.toString())
//            mBundle.putString("email",binding.inputEmail.text.toString())
//            mBundle.putString("dateBirth",binding.inputDate.text.toString())
//            mBundle.putString("phoneNumber",binding.inputPhone.text.toString())
//            moveLogin.putExtra("register",mBundle)

//            CoroutineScope(Dispatchers.IO).launch {
//                db.userDao().addUser(
//                    User(0, binding.inputUsername.text.toString(), binding.inputPassword.text.toString(), binding.inputEmail.text.toString(),binding.inputDate.text.toString(),binding.inputPhone.text.toString())
//                )
//                finish()
//            }
            Register()
            sendNotification1()
            createNotificationChannel()

        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name,NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification1(){

        val broadcastIntent: Intent = Intent(this,LoginActivity::class.java)
        val actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val gambar = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.salonku)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Selamat")
            .setContentText("Akun Anda Berhasil Di Register")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(gambar))
            .addAction(R.mipmap.ic_launcher, "Login",actionIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1, builder.build())
        }
    }






    private fun Register(){

        val user = User(
            binding.inputUsername.text.toString(),
            binding.inputPassword.text.toString(),
            binding.inputEmail.text.toString(),
            binding.inputDate.text.toString(),
            binding.inputPhone.text.toString()
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, PesananApi.register, Response.Listener { response ->
                val gson = Gson()
                var mahasiswa = gson.fromJson(response, User::class.java)

                if(mahasiswa != null)
                    Toast.makeText(this@RegisterActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                val mBundle = Bundle()
                val moveLogin = Intent(this, LoginActivity::class.java)
                mBundle.putString("username", binding.inputUsername.text.toString())
                mBundle.putString("password", binding.inputPassword.text.toString())
                mBundle.putString("email",binding.inputEmail.text.toString())
                mBundle.putString("dateBirth",binding.inputDate.text.toString())
                mBundle.putString("phoneNumber",binding.inputPhone.text.toString())
                moveLogin.putExtra("register",mBundle)
                startActivity(moveLogin)
                finish()


            }, Response.ErrorListener { error ->

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        queue!!.add(stringRequest)
    }


}