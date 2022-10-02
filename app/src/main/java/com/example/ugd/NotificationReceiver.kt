package com.example.ugd

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            if ("REPLY_ACTION".equals(action)){
                val message = replyMessage(this)
                val messageId = getIntExtra("KEY_MESSAGE_ID",0)
                Toast.makeText(context,"$messageId : $message",Toast.LENGTH_LONG).show()
            }

            context?.apply {
                val notificationId = getIntExtra("KEY_NOTIFICATION_ID",0)
                val channelId = getStringExtra("KEY_CHANNEL_ID")

                // Build a notification and add the action
                val builder = NotificationCompat.Builder(this, channelId.toString())
                    .setSmallIcon(R.drawable.ic_send_24)
                    .setContentTitle("Successful")
                    .setContentText("Message sent!")

                // Finally, issue the notification
                NotificationManagerCompat.from(this).apply {
                    notify(notificationId, builder.build())
                }
            }
        }
    }


    private fun replyMessage(intent: Intent): CharSequence? {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        return remoteInput?.getCharSequence("KEY_TEXT_REPLY")
    }
}

