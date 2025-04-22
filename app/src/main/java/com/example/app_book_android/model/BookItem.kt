package com.example.app_book_android.model

import kotlinx.serialization.Serializable

@Serializable
data class BookItem(
    val id: String,
    val kind: String? = null,
    val volumeInfo: VolumeInfo? = null
)