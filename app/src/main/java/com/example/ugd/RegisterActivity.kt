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
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ugd.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var btnSignUp : Button

    private var binding: ActivityRegisterBinding? = null
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        username = findViewById(R.id.inputUsername)
        password = findViewById(R.id.inputPassword)

        btnSignUp = findViewById(R.id.btnSignUp)

        createNotificationChannel()

        btnSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("username", username.text.toString())
            mBundle.putString("password", password.text.toString())
            intent.putExtra("register", mBundle)
            sendNotification1()
            startActivity(intent)
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
}