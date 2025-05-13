package com.example.app_book_android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_book_android.model.NotificationBook
import com.example.app_book_android.network.BookService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val bookService: BookService
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationBook>>(emptyList())
    val notifications: StateFlow<List<NotificationBook>> = _notifications

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadNotifications(userId: String) {
        bookService.getAllNotifications(_notifications, userId, _isLoading)
    }
}