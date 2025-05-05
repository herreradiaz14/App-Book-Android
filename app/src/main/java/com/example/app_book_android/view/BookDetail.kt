package com.example.app_book_android.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_book_android.viewmodel.BookDetailViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.app_book_android.ui.theme.PurplePrimary
import com.example.app_book_android.utils.BookStatus
import com.example.app_book_android.utils.Constants
import kotlin.toString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetail(
    bookId: String, navController: NavController, viewModel: BookDetailViewModel = hiltViewModel()
) {
    val book by viewModel.book
    var showDialog by remember { mutableStateOf(false) }
    var pageInput by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(bookId) {
        viewModel.loadBookById(bookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del libro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (book == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PurplePrimary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = book?.title ?: "Sin título",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Autor: ${book?.author ?: "Sin autor"}",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Editorial: ${book?.publisher ?: "No registrado"}",
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Publicado en: ${book!!.publishedDate ?: "-"}",
                        fontSize = 14.sp
                    )

                    val thumbnail = book?.thumbnail.toString()
                    BookImage(thumbnail = thumbnail, thumbnailHeight = 120.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    val statusEnum = BookStatus.fromKey(book?.status ?: Constants.TO_READ)
                    Text(text = "Estado: ${statusEnum.displayName}")
                    ProgressIndicator(book=book!!)
                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            inputError = null
                            pageInput = book?.currentPage.toString()
                            showDialog = true
                        },
                        modifier = Modifier
                            .align(Alignment.Start),
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                    ) { Text("Registrar progreso") }

                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
                    )
                    Text(
                        text = "Descripción del libro:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = book?.description ?: "Sin descripción",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(80.dp))
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            TextButton(
                                // Validación para páginas leídas
                                onClick = {
                                    val pages = pageInput.toIntOrNull()
                                    if (pages == null || pages < 0) {
                                        inputError = "Ingresa un número válido"
                                    } else if (pages > book!!.totalPages) {
                                        inputError = "No puede superar ${book!!.totalPages} páginas"
                                    } else {
                                        val statusInitial = if (pages == 0) Constants.TO_READ else Constants.IN_PROGRESS
                                        val newStatus = if (pages == book!!.totalPages) Constants.COMPLETED else statusInitial
                                        viewModel.updateProgressAndStatus(bookId, pages, newStatus)
                                        showDialog = false
                                    }
                                }
                            ) { Text("Guardar") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancelar")
                            }
                        },
                        title = { Text("Actualizar progreso") },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = pageInput,
                                    onValueChange = { pageInput = it },
                                    label = { Text("Páginas leídas") },
                                    singleLine = true,
                                    isError = inputError != null,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                inputError?.let {
                                    Text(it, color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
