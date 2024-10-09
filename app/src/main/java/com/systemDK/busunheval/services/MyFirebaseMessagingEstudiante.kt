package com.systemDK.busunheval.services

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.systemDK.busunheval.R
import com.systemDK.busunheval.channel.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingEstudiante: FirebaseMessagingService() {

    private val NOTIFICATION_CODE = 100

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data
        val title = data["title"]
        val body = data["body"]

        if (!title.isNullOrBlank() && !body.isNullOrBlank()){

            showNotification(title, body)

        }
    }

    private fun showNotification(title: String, body: String){
        val helper = NotificationHelper(baseContext)
        val builder = helper.getNotificacion(title, body)
        helper.getManager().notify(1,builder.build())
    }


}