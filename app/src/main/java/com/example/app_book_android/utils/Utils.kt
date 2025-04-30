package com.example.app_book_android.utils

fun convertSecureURL(url: String): String {
    return if (url.startsWith("http://", ignoreCase = true)) {
        url.replaceFirst("http://", "https://", ignoreCase = true)
    } else {
        url
    }
}