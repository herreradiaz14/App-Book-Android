package com.example.app_book_android.view

import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_book_android.model.BookItem
import com.example.app_book_android.ui.theme.PurplePrimary
import com.example.app_book_android.utils.convertSecureURL
import com.example.app_book_android.viewmodel.BookItemRowViewModel

@Composable
fun BookItemRow(bookItem: BookItem, viewModel: BookItemRowViewModel = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    val bookIsSave = viewModel.savedBookIds.contains(bookItem.id)

    LaunchedEffect(bookItem.id) {
        viewModel.isBookSave(bookItem.id ?: "")
    }

    if (showDialog) {
        BookDialogConfirm(
            bookId = bookItem.id ?: "",
            onDismiss = { showDialog = false },
            onSaveToList = {
                viewModel.saveBookToMyList()
                showDialog = false
            },
            viewModel = viewModel
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Se realiza esto ya que las imágenes vienen con http
        val imageUrl = bookItem.volumeInfo?.imageLinks?.thumbnail.toString()
        val secureImageUrl = convertSecureURL(imageUrl)
        BookImage(thumbnail = secureImageUrl, thumbnailHeight = 40.dp, modifier = Modifier)

        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            val authors = bookItem.volumeInfo?.authors?.joinToString(", ") ?: "Autor no disponible"
            Text(
                text = bookItem.volumeInfo?.title ?: "Título no disponible",
                style = MaterialTheme.typography.titleMedium
            )

            val publishedYear = bookItem.volumeInfo?.publishedDate?.split("-")?.firstOrNull()
            val year = if (publishedYear != null) "$publishedYear. " else ""

            Text(
                text = "$year$authors",
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (!bookIsSave) {
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Agregar a mi lista",
                    tint = PurplePrimary
                )
            }
        }
    }
}

@Composable
fun BookDialogConfirm(
    bookId: String,
    onDismiss: () -> Unit,
    onSaveToList: () -> Unit,
    viewModel: BookItemRowViewModel
) {
    val book by viewModel.book
    val isLoading by viewModel.isLoading
    val error by viewModel.errorMessage

    LaunchedEffect(bookId) {
        viewModel.getBookApiDetail(bookId)
    }

    val bookVolumeInfo = book?.volumeInfo

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            if(bookVolumeInfo != null){
                TextButton(onClick = onSaveToList) {
                    Text("Agregar a mi lista")
                }
            }
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text(book?.volumeInfo?.title?: "Detalle de libro") },
        text = {
            when {
                isLoading -> Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = PurplePrimary)
                }
                error != null -> Text(error ?: "Ha ocurrido un error")
                book != null -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if(bookVolumeInfo != null){
                            BookImage(
                                thumbnail = bookVolumeInfo.imageLinks?.thumbnail?:"",
                                thumbnailHeight = 100.dp,
                                modifier = Modifier
                            )
                            val author = bookVolumeInfo.authors?.joinToString(", ") ?: "Autor no disponible"
                            Text("Autor: $author")
                            Text("Publicado en: ${bookVolumeInfo.publishedDate}")
                        } else {
                            Text("Sin información que mostrar")
                        }
                    }
                }
            }
        }
    )
}
