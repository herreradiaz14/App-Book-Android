package com.example.app_book_android.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class BookItem(
    val id: String? = null,
    val kind: String? = null,
    val authors: List<String>? = null,
    val smallThumbnail: String? = null,
    val title: String? = null
)