package com.example.app_book_android.network

import javax.inject.Inject

class Authentication @Inject constructor(private val firebase: FirebaseClient) {
    suspend fun login(email: String, password: String){
        firebase.auth.signInWithEmailAndPassword(email, password)
    }
}