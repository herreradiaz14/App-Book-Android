package com.example.app_book_android.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class BookSearchResponse(
    val items: List<BookItem>? = null,
    val kind: String? = null,
    val totalItems: Int? = null
)