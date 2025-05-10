package com.example.app_book_android.utils

import android.text.Html

fun convertSecureURL(url: String): String {
    return if (url.startsWith("http://", ignoreCase = true)) {
        url.replaceFirst("http://", "https://", ignoreCase = true)
    } else {
        url
    }
}

fun parseHtmlToText(html: String): String {
    var textConvert = html

    try {
        textConvert = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }

    return textConvert
}