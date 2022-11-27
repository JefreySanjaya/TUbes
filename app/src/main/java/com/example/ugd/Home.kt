package com.example.ugd

import android.app.*
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.ugd.room.NoteDB
import com.example.ugd.room.Note
import com.example.ugd.room.Constant
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.RemoteInput
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.jar.Manifest


class Home : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    val db by lazy { NoteDB(this) }
    lateinit var noteAdapter: NoteAdapter

    //private val CHANNEL_ID_2 = "channel_notification_02"
    val channelId = "My_Channel_ID"
    val notificationId = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnRequestUser = findViewById<Button>(R.id.btnRequestUser)

        btnRequestUser.setOnClickListener{
            requestPermission()
        }

        loadFragment(FragmentHome())
        bottomNav = findViewById(R.id.bottomNavigationView) as BottomNavigationView
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.Home-> {
                    loadFragment(FragmentHome())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.account -> {
                    loadFragment(FragmentAccount())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.list_model -> {
                    loadFragment(FragmentList())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_exit -> {
                    loadFragment(FragmentExit())
                    val builder : AlertDialog.Builder = AlertDialog.Builder(this@Home)
                    builder.setMessage("Are You Sure Want To Exit")
                        .setPositiveButton("Yes",object : DialogInterface.OnClickListener{
                            override fun onClick(dialogInterface: DialogInterface, i:Int) {
                                finishAndRemoveTask()
                                createNotificationChannel(String())
                            }
                        })
                        .show()

                }
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun createNotificationChannel(channelId:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "My Channel"
            val channelDescription = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId,name,importance)
            channel.apply {
                description = channelDescription
            }

            // Finally register the channel with system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= 24) {
            // Create an instance of remote input builder
            val remoteInput: RemoteInput = RemoteInput.Builder("KEY_TEXT_REPLY")
                .run {
                    setLabel("Write your message here")
                    build()
                }

            // Create an intent
            val intent = Intent(this, NotificationReceiver::class.java)
            intent.action = "REPLY_ACTION"
            intent.putExtra("KEY_NOTIFICATION_ID", notificationId)
            intent.putExtra("KEY_CHANNEL_ID", channelId)
            intent.putExtra("KEY_MESSAGE_ID", 1)

            // Create a pending intent for the reply button
            val replyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
                this,
                101,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Create reply action and add the remote input
            var action: NotificationCompat.Action = NotificationCompat.Action.Builder(
                R.drawable.ic_reply_icon,
                "Reply",
                replyPendingIntent
            ).addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build()


            // Build a notification and add the action
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("ThankYou!")
                .setContentText("Hello, Thanks for being here. Because of you, I get to do my dream work. I wake up each day with one mission: to help you see the beauty in you that I see. ")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Hello, Thanks for being here. Because of you, I get to do my dream work. I wake up each day with one mission: to help you see the beauty in you that I see."))
                .setColor(Color.BLUE)
                .addAction(action)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            // Finally, issue the notification
            NotificationManagerCompat.from(this).apply {
                notify(notificationId, builder.build())
            }
        }
    }

    private fun writeExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
          PackageManager.PERMISSION_GRANTED

    private fun LocationPermission() =
        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    private fun LocationBackgroundPermission() =
        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    private fun requestPermission(){
        var requestPermissionUser = mutableListOf<String>()
        if (!writeExternalStoragePermission()){
            requestPermissionUser.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!LocationPermission()){
            requestPermissionUser.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!LocationBackgroundPermission()){
            requestPermissionUser.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        if(requestPermissionUser.isNotEmpty()){
            ActivityCompat.requestPermissions(this, requestPermissionUser.toTypedArray(),0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()){
            for (i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d("PermissionUser", "${permissions} yang anda jalankan berhasil...")
                }
            }
        }
    }

    private fun sendNotification2(){

}



}