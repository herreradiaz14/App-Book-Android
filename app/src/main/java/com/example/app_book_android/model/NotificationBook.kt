package com.example.app_book_android.model

import com.google.firebase.Timestamp

data class NotificationBook(
    val title: String = "",
    val body: String = "",
    val timestamp: Timestamp? = null
)