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

    private val _isPaginating = mutableStateOf(false)
    val isPaginating: State<Boolean> = _isPaginating

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    var totalItems: Int? = null
    private var currentPage = 0
    private val maxResultsPerPage = 10

    private var searchJob: Job? = null

    fun searchBooks(query: String) {
        searchJob?.cancel()
        _searchQuery.value = query
        _books.value = emptyList()
        currentPage = 0

        if (query.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(500.milliseconds)
                searchByPagination(query)
            }
        } else {
            _books.value = emptyList()
        }
    }

    fun searchByPagination(query: String, addListItem: Boolean = false ){
        try {
            _errorMessage.value = null
            // No cargar más si: ya se está paginando; carga inicial (para no agregar a la lista); Si se llega al final del total
            if (_isPaginating.value || (_isLoading.value && addListItem.not()) || (totalItems != null && _books.value.size >= totalItems!!)) {
                return
            }

            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                if (addListItem) {
                    _isPaginating.value = true
                } else {
                    _isLoading.value = true
                }

                val startIndex = currentPage * maxResultsPerPage
                val result = apiService.searchBooks(query, startIndex, maxResultsPerPage)

                totalItems = result.totalItems

                val newItems = result.items ?: emptyList()
                _books.value = if (addListItem) {
                    _books.value + newItems
                } else {
                    newItems
                }

                currentPage++
                _isLoading.value = false
                _isPaginating.value = false
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            _errorMessage.value = "No se han podido cargar los libros"
        }
    }

    fun loadMoreBooks() {
        if (_searchQuery.value.isNotBlank()) {
            searchByPagination(_searchQuery.value, addListItem = true)
        }
    }
}