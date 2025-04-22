package com.example.app_book_android.model

import kotlinx.serialization.Serializable

@Serializable
data class VolumeInfo(
    val authors: List<String>? = null,
    val title: String? = null,
    val publishedDate: String? = null,
    val imageLinks: ImageLinks? = null
)