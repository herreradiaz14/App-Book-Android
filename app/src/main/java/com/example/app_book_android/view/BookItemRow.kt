package com.example.app_book_android.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
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
        val imageUrl = bookItem.volumeInfo?.imageLinks?.thumbnail
        if (!imageUrl.isNullOrBlank()) {
            // Se realiza esto ya que las imágenes vienen con http
            val secureImageUrl = convertSecureURL(imageUrl)

            AsyncImage(
                model = secureImageUrl,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        } else {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = null,
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