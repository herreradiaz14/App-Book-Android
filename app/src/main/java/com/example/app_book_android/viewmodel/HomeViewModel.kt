package com.example.app_book_android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_book_android.model.Book
import com.example.app_book_android.network.FirebaseClient
import com.example.app_book_android.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseClient: FirebaseClient
) : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    init {
        getAllBooks()
    }

    private fun getAllBooks() {
        firebaseClient.db.collection(Constants.BOOKS_COLLECTION)
            .addSnapshotListener { snapshots, e ->
                if (e != null || snapshots == null) return@addSnapshotListener
                _books.value = snapshots.mapNotNull { doc ->
                    doc.toObject(Book::class.java).copy(id = doc.id)
                }
            }
    }
}