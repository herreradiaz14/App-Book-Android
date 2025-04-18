package com.example.app_book_android.service

import com.example.app_book_android.model.remote.BookItem
import com.example.app_book_android.model.remote.BookSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class AppBookApiService @Inject constructor(
    private val client: HttpClient,
    private val googleBooksUrl: String
) {
    private val volumesEndpoint = "${googleBooksUrl}volumes"

    suspend fun searchBooks(query: String, startIndex: Int? = 0, maxResults: Int? = 10): BookSearchResponse? {
        return try {
            client.get(volumesEndpoint) {
                parameter("q", query)
                parameter("startIndex", startIndex)
                parameter("maxResults", maxResults)
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}