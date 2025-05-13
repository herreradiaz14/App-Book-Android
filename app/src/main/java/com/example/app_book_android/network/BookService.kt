package com.example.app_book_android.network

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.app_book_android.model.Book
import com.example.app_book_android.model.NotificationBook
import com.example.app_book_android.utils.Constants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class BookService @Inject constructor(private val firebaseClient: FirebaseClient) {
    fun getAllBooks(
        books: MutableStateFlow<List<Book>>,
        userId: String,
        isLoading: MutableStateFlow<Boolean>? = null
    ) {
        firebaseClient.db.collection(Constants.BOOKS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_COLLECTION)
            .addSnapshotListener { snapshots, e ->
                if (e != null || snapshots == null) return@addSnapshotListener
                books.value = snapshots.mapNotNull { doc ->
                    doc.toObject(Book::class.java).copy(id = doc.id)
                }
                isLoading?.value = false
            }
    }

    fun addBook(book: Book, savedBookIds: SnapshotStateList<String>? = null) {
        val userId = firebaseClient.auth.currentUser?.uid ?: Constants.GUEST

        firebaseClient.db.collection(Constants.BOOKS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_COLLECTION)
            .add(book)
            .addOnSuccessListener { snapshot ->
                if (savedBookIds != null && !book.idGoogle.isNullOrBlank() && !savedBookIds.contains(
                        book.idGoogle
                    )
                ) {
                    savedBookIds.add(book.idGoogle)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al guardar libro", e)
            }
    }

    fun loadBookByIdGoogle(
        idGoogle: String,
        book: MutableState<Book?>,
        savedBookIds: SnapshotStateList<String>? = null,
        errorMessage: MutableState<String>? = null
    ) {
        val userId = firebaseClient.auth.currentUser?.uid ?: Constants.GUEST

        firebaseClient.db.collection(Constants.BOOKS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_COLLECTION)
            .whereEqualTo("idGoogle", idGoogle)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                book.value = snapshot.documents.firstOrNull()?.toObject(Book::class.java)

                if (savedBookIds != null && !snapshot.isEmpty && !savedBookIds.contains(idGoogle)) {
                    savedBookIds.add(idGoogle)
                }
            }
            .addOnFailureListener { e ->
                errorMessage?.value = "Error al cargar el libro"
                Log.e("BookDetailViewModel", "Error al cargar el libro $e")
            }
    }

    fun updateProgress(idGoogle: String, currentPage: Int, status: String?, book: MutableState<Book?>) {
        val userId = firebaseClient.auth.currentUser?.uid ?: Constants.GUEST

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

    fun deleteBook(idGoogle: String) {
        val userId = firebaseClient.auth.currentUser?.uid ?: Constants.GUEST

        firebaseClient.db.collection(Constants.BOOKS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_COLLECTION)
            .whereEqualTo("idGoogle", idGoogle)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val doc = snapshot.documents.firstOrNull()
                doc?.reference?.delete()
                    ?.addOnSuccessListener {
                        Log.d("Firestore", "Libro eliminado correctamente")
                    }
                    ?.addOnFailureListener {
                        Log.e("Firestore", "Error al eliminar libro", it)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al eliminar libro", e)
            }
    }

    fun saveNotification(title: String, body: String) {
        val userId = firebaseClient.auth.currentUser?.uid ?: Constants.GUEST

        val notification = hashMapOf(
            "title" to title,
            "body" to body,
            "timestamp" to FieldValue.serverTimestamp()
        )

        firebaseClient.db.collection(Constants.NOTIFICATIONS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_NOTIFICATION)
            .add(notification)
            .addOnSuccessListener {
                Log.d("Notification", "Notificación guardada")
            }
            .addOnFailureListener {
                Log.e("Notification", "Error al guardar la notificación")
            }
    }

    fun getAllNotifications(
        notifications: MutableStateFlow<List<NotificationBook>>,
        userId: String,
        isLoading: MutableStateFlow<Boolean>? = null
    ) {
        firebaseClient.db.collection(Constants.NOTIFICATIONS_COLLECTION)
            .document(userId)
            .collection(Constants.MY_NOTIFICATION)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                isLoading?.value = false
                if (e != null || snapshots == null) return@addSnapshotListener
                notifications.value = snapshots.mapNotNull { it.toObject(NotificationBook::class.java) }
            }
    }
}