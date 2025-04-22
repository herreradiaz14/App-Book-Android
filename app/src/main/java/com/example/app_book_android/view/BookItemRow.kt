package com.example.app_book_android.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.app_book_android.model.BookItem

@Composable
fun BookItemRow(bookItem: BookItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = bookItem.volumeInfo?.imageLinks?.thumbnail
        if (!imageUrl.isNullOrBlank()) {
            // Se realiza esto ya que las imágenes vienen con http
            val secureImageUrl = if (imageUrl.startsWith("http://", ignoreCase = true)) {
                imageUrl.replaceFirst("http://", "https://", ignoreCase = true)
            } else {
                imageUrl
            }

            AsyncImage(
                model = secureImageUrl,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.AccountBox,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            val authors = bookItem.volumeInfo?.authors?.joinToString(", ") ?: "Autor no disponible"
            val publishedYear = bookItem.volumeInfo?.publishedDate?.split("-")?.firstOrNull() ?: ""
            Text(text = bookItem.volumeInfo?.title ?: "Título no disponible", style = MaterialTheme.typography.titleMedium)
            Text(text = "$publishedYear. $authors", style = MaterialTheme.typography.bodySmall)
        }
    }
}