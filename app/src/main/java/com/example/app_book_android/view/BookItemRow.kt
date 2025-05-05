package com.example.app_book_android.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_book_android.model.BookItem
import com.example.app_book_android.utils.convertSecureURL

@Composable
fun BookItemRow(bookItem: BookItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Se realiza esto ya que las imágenes vienen con http
        val imageUrl = bookItem.volumeInfo?.imageLinks?.thumbnail.toString()
        val secureImageUrl = convertSecureURL(imageUrl)
        BookImage(thumbnail = secureImageUrl, thumbnailHeight = 40.dp)

        Spacer(modifier = Modifier.width(8.dp))
        Column {
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
    }
}