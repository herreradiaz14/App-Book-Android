package com.example.app_book_android.utils

object Constants {
    const val BOOKS_COLLECTION = "books"
    const val NOTIFICATIONS_COLLECTION = "notifications"
    const val MY_COLLECTION = "my"
    const val MY_NOTIFICATION = "received"
    const val GUEST = "guest"
    const val COMPLETED = "completed"
    const val TO_READ = "to_read"
    const val IN_PROGRESS = "in_progress"
    const val CHANNEL_ID = "channel_id"
    const val CHANNEL_NAME = "Notificaciones BookApp"
}

enum class BookStatus(val key: String, val displayName: String) {
    ToRead(Constants.TO_READ, "Por leer"),
    InProgress(Constants.IN_PROGRESS, "En progreso"),
    Completed(Constants.COMPLETED, "Leído");

    companion object {
        fun fromKey(key: String): BookStatus =
            entries.find { it.key == key } ?: ToRead
    }
}