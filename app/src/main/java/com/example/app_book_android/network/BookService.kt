package com.example.app_book_android.network

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.app_book_android.model.Book
import com.example.app_book_android.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class BookService @Inject constructor(private val firebaseClient: FirebaseClient) {
    val userId = firebaseClient.auth.currentUser?.uid ?: Constants.GUEST

    fun getAllBooks(books: MutableStateFlow<List<Book>>) {
        firebaseClient.db.collection(Constants.BOOKS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_COLLECTION)
            .addSnapshotListener { snapshots, e ->
                if (e != null || snapshots == null) return@addSnapshotListener
                books.value = snapshots.mapNotNull { doc ->
                    doc.toObject(Book::class.java).copy(id = doc.id)
                }
            }
    }

    fun addBook(book: Book) {
        firebaseClient.db.collection(Constants.BOOKS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_COLLECTION)
            .add(book)
            .addOnSuccessListener {
                Log.d("Firestore", "Libro guardado exitosamente")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al guardar libro", e)
            }
    }

    fun loadBookByIdGoogle(idGoogle: String, book: MutableState<Book?>) {
        firebaseClient.db.collection(Constants.BOOKS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_COLLECTION)
            .whereEqualTo("idGoogle", idGoogle)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                book.value = snapshot.documents.firstOrNull()?.toObject(Book::class.java)
            }
            .addOnFailureListener { e ->
                Log.e("BookDetailViewModel", "Error al cargar el libro", e)
            }
    }

    fun updateProgress(idGoogle: String, currentPage: Int, status: String?, book: MutableState<Book?>) {
        firebaseClient.db.collection(Constants.BOOKS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_COLLECTION)
            .whereEqualTo("idGoogle", idGoogle)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val doc = snapshot.documents.firstOrNull() ?: return@addOnSuccessListener
                doc.reference.update(
                    mapOf(
                        "currentPage" to currentPage,
                        "status" to status
                    )
                ).addOnSuccessListener {
                    val updated = book.value?.copy(currentPage = currentPage, status = status)
                    book.value = updated
                }
            }
    }
}