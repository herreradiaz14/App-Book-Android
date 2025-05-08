package com.example.app_book_android.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_book_android.model.Book
import com.example.app_book_android.model.BookItem
import com.example.app_book_android.network.BookService
import com.example.app_book_android.service.AppBookApiService
import com.example.app_book_android.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookItemRowViewModel @Inject constructor(
    private val apiService: AppBookApiService,
    private val bookService: BookService,
) : ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _book = mutableStateOf<BookItem?>(null)
    val book: State<BookItem?> = _book

    private val _currentBook = mutableStateOf<Book?>(null)

    private val _savedBookIds = mutableStateListOf<String>()
    val savedBookIds: List<String> = _savedBookIds

    fun getBookApiDetail(bookId: String) {
        try {
            _isLoading.value = true
            _errorMessage.value = null

            viewModelScope.launch {
                val result = apiService.getBookById(bookId)
                _isLoading.value = false
                _book.value = result
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            _errorMessage.value = "No se ha podido cargar el libro"
        }
    }

    fun isBookSave(bookApiId: String) {
        bookService.loadBookByIdGoogle(
            idGoogle =  bookApiId,
            book = _currentBook,
            savedBookIds = _savedBookIds
        )
    }

    fun saveBookToMyList() {
        val idGoogle = _book.value?.id ?: ""
        val bookCreate = _book.value?.volumeInfo
        val book = Book(
            title = bookCreate?.title,
            author = bookCreate?.authors?.joinToString(),
            totalPages = bookCreate?.pageCount?: 0,
            currentPage = 0,
            status = Constants.TO_READ,
            thumbnail = bookCreate?.imageLinks?.thumbnail,
            idGoogle = idGoogle,
            description = bookCreate?.description,
            publishedDate = bookCreate?.publishedDate,
            publisher = bookCreate?.publisher
        )

        bookService.addBook(book, _savedBookIds)
    }
}