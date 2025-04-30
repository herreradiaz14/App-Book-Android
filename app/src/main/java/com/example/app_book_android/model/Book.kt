package com.example.app_book_android.model

data class Book(
    val id: String? = null,
    val title: String? = null,
    val author: String? = null,
    val totalPages: Int = 0,
    val currentPage: Int = 0,
    val status: String? = null,
    val thumbnail: String? = null,
    val idGoogle: String? = null
)