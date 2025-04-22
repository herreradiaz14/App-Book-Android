package com.example.app_book_android.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)