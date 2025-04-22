package com.example.app_book_android.model

import kotlinx.serialization.Serializable

@Serializable
data class BookResponse(
    val items: List<BookItem>? = null,
    val kind: String? = null,
    val totalItems: Int? = null
)