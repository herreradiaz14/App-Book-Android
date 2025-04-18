package com.example.app_book_android

import com.example.app_book_android.service.AppBookApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/"

    @Provides
    @Singleton
    fun ktorClient(): HttpClient {
        return HttpClient(CIO){
            install(ContentNegotiation){
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Provides
    @Singleton
    fun appBookApiService(client: HttpClient): AppBookApiService {
        return AppBookApiService(client, GOOGLE_BOOKS_URL)
    }
}