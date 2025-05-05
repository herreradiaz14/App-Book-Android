package com.example.app_book_android.service

import com.example.app_book_android.model.BookResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class AppBookApiService @Inject constructor(
    private val client: HttpClient
) {
    // private val volumesEndpoint = "https://www.googleapis.com/books/v1/volumes"
    private val volumesEndpoint = "https://3aa00786-32d5-4d8f-bbeb-5900c893081c.mock.pstmn.io/books"

    suspend fun searchBooks(query: String, startIndex: Int? = 0, maxResults: Int? = 10): BookResponse {
        val response: HttpResponse = client.get(urlString = volumesEndpoint) {
            parameter("q", query)
            parameter("startIndex", startIndex)
            parameter("maxResults", maxResults)
            // Api key generado de la consola de Google para Books API
            parameter("key", "...")
        }
        return response.body()
    }
}