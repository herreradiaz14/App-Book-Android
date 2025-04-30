package com.example.app_book_android.network

import com.example.app_book_android.model.BookItem
import com.example.app_book_android.utils.Constants
import javax.inject.Inject

class BookService @Inject constructor(private val firebase: FirebaseClient) {
    suspend fun addBook(book: BookItem) = runCatching {
        val userId = firebase.auth.currentUser?.uid
        if (userId != null) {
            firebase.db
                .collection(Constants.BOOKS_COLLECTION)
                .add(book)
        }
    }.isSuccess
}