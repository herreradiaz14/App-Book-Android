package com.example.app_book_android.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_book_android.viewmodel.SearchViewModel

@Composable
fun Search(viewModel: SearchViewModel = hiltViewModel()) {
    val books by viewModel.books.collectAsState()
    val searchQuery by viewModel.searchQuery
    val isLoading by viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = searchQuery,
            onValueChange = viewModel::searchBooks,
            leadingIcon = { Icon(imageVector =  Icons.Filled.Search, contentDescription = "Buscar")},
            placeholder = {
                Text(text = "Ingrese un título o autor")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 90.dp)
                .clip(RoundedCornerShape(30.dp))
                .border(2.dp, Color.White, RoundedCornerShape(30.dp)),
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black, fontSize = 20.sp
            ),

        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            println(books)
            if (books.isEmpty() && searchQuery.isNotBlank()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null
                    )
                    Text(
                        text = "Sin resultados que mostrar",
                    )
                }
            } else {
                LazyColumn {
                    items(books, key = {it.id}) { bookItem ->
                        BookItemRow(bookItem)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}