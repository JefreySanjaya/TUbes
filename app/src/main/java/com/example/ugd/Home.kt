package com.example.ugd

import android.app.*
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.RemoteInput

class Home : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    //private val CHANNEL_ID_2 = "channel_notification_02"
    val channelId = "My_Channel_ID"
    val notificationId = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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


}

    private fun sendNotification2(){

}
