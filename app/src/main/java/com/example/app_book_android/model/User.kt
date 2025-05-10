package com.example.app_book_android.model


import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String?,
    val email: String?
)