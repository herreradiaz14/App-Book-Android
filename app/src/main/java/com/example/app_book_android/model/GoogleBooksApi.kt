package com.example.app_book_android.model.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int? = 0,
        @Query("maxResults") maxResults: Int? = 15
    ): BookSearchResponse
}