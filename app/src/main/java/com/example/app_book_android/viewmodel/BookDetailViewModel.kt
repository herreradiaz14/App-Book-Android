package com.example.app_book_android.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.app_book_android.model.Book
import com.example.app_book_android.network.BookService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookService: BookService
) : ViewModel() {
    private val _book = mutableStateOf<Book?>(null)
    val book = _book as State<Book?>

    fun loadBookById(bookId: String) {
        bookService.loadBookByIdGoogle(bookId, _book)
    }

    fun updateProgressAndStatus(idGoogle: String, currentPage: Int, status: String?) {
        bookService.updateProgress(idGoogle, currentPage, status, _book)
    }

    fun deleteBook(bookId: String) {
        bookService.deleteBook(bookId)
    }
}