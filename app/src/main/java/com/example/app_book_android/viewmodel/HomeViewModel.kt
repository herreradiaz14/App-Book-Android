package com.example.app_book_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.app_book_android.model.Book
import com.example.app_book_android.navigation.BottomNavigation
import com.example.app_book_android.network.BookService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val bookService: BookService
) : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    fun loadBooksForUser(userId: String) {
        bookService.getAllBooks(_books, userId)
    }

    fun viewBookDetail(navController: NavHostController, bookId: String) {
        navController.navigate("${BottomNavigation.BookDetail}/${bookId}")
    }
}