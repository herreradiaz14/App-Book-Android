package com.example.app_book_android.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Home() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Inicio")
    }
}