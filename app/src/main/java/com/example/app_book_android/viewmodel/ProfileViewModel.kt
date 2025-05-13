package com.example.app_book_android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_book_android.model.User
import com.example.app_book_android.network.FirebaseClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseClient: FirebaseClient
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Result<Unit>>(Result.success(Unit))
    val loginResult: StateFlow<Result<Unit>> = _loginResult

    private val _user = MutableStateFlow<User?>(null)
    val user : StateFlow<User?> = _user

    fun signInWithEmailAndPassword(email: String, password: String) {
        try {
            firebaseClient.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginResult.value = Result.success(Unit)
                        val loggedUser = firebaseClient.auth.currentUser
                        _user.value = User(id = loggedUser?.uid, email = loggedUser?.email)
                    } else {
                        _loginResult.value = Result.failure(task.exception ?: Exception("Error"))
                    }
                }
        } catch (e: Exception) {
            _loginResult.value = Result.failure(Exception("Ingrese un usuario y contraseña válidos"))
            println("Error: ${e.message}")
        }
    }

    fun logout() {
        firebaseClient.auth.signOut()
        _user.value = null
    }

    fun checkCurrentUser() {
        val current = firebaseClient.auth.currentUser
        if (current != null) {
            _user.value = User(id = current.uid, email = current.email)
        } else {
            _user.value = null
        }
    }

}