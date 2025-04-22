package com.example.app_book_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_book_android.model.BookItem
import com.example.app_book_android.service.AppBookApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val apiService: AppBookApiService
) : ViewModel() {
    private val _books = MutableStateFlow<List<BookItem>>(emptyList())
    val books: StateFlow<List<BookItem>> = _books

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var searchJob: Job? = null

    fun searchBooks(query: String) {
        searchJob?.cancel()
        _searchQuery.value = query

        if (query.isNotBlank()) {
            searchJob = viewModelScope.launch {
                _isLoading.value = true
                delay(500.milliseconds)
                try {
                    val result = apiService.searchBooks(query)
                    _books.value = result.items ?: emptyList()
                } catch (e: Exception){
                    println(e.message)
                    _books.value = emptyList()
                }
                _isLoading.value = false
            }
        } else {
            _books.value = emptyList()
        }
    }
}