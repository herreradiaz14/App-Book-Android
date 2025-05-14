package com.example.app_book_android.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.app_book_android.network.BookService
import com.example.app_book_android.network.FirebaseClient
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.app_book_android.utils.Constants
import kotlin.random.Random

class NotificationService: FirebaseMessagingService() {
    private val random = Random

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { message ->
            Log.i("Notification Title", "${message.title}")
            Log.i("Notification Body", "${message.body}")

            // Verificar si el mensaje tiene un ID de libro
            val idGoogle = if (remoteMessage.data.isNotEmpty()) {
                remoteMessage.data["idGoogle"]
            } else ""
            Log.i("Notification Data", "idGoogle = $idGoogle")
            showNotification(message)
            saveNotificationUser(message, idGoogle?:"")
        }
    }

    private fun showNotification(message: RemoteMessage.Notification) {
        val channelId = Constants.CHANNEL_ID
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        manager.notify(random.nextInt(), notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d("Notification","New token: $token")
    }

    private fun saveNotificationUser(
        message: RemoteMessage.Notification, idGoogle: String
    ){
        val bookService = BookService(firebaseClient = FirebaseClient())
        bookService.saveNotification(
            title = message.title.toString(),
            body = message.body.toString(),
            idGoogle = idGoogle
        )
    }
}