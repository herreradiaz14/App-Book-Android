package com.example.app_book_android.view

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.app_book_android.model.Book
import com.example.app_book_android.ui.theme.PurplePrimary
import com.example.app_book_android.utils.BookStatus
import com.example.app_book_android.utils.Constants
import com.example.app_book_android.utils.parseHtmlToText
import kotlin.toString

@Composable
fun BookDetail(
    bookId: String, navController: NavController, viewModel: BookDetailViewModel = hiltViewModel()
) {
    val book by viewModel.book

    LaunchedEffect(bookId) {
        viewModel.loadBookById(bookId)
    }

    BookDetailComplete(
        book = book,
        navController = navController,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailComplete(book: Book?, navController: NavController, viewModel: BookDetailViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var pageInput by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
        val context = LocalContext.current

        if(viewModel.actionMessage.value.toString().isNotBlank()){
            Toast.makeText(
                context,
                viewModel.actionMessage.value,
                Toast.LENGTH_LONG
            ).show()
        }

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
                        text = book.title ?: "Sin título",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp),
                        textAlign = TextAlign.Center
                    )

                    val thumbnail = book.thumbnail.toString()
                    BookImage(
                        thumbnail = thumbnail,
                        thumbnailHeight = 160.dp,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    )

                    Text(
                        text = "Autor: ${book.author ?: "Sin autor"}",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Editorial: ${book.publisher ?: "No registrado"}",
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Publicado en: ${book.publishedDate ?: "-"}",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val statusEnum = BookStatus.fromKey(book.status ?: Constants.TO_READ)
                    Text(text = "Estado: ${statusEnum.displayName}")
                    ProgressIndicator(book=book)
                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically // Optional: Align items vertically in the center
                    ) {
                        Button(
                            onClick = {
                                inputError = null
                                pageInput = book.currentPage.toString()
                                showDialog = true
                            },
                            modifier = Modifier,
                            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                        ) {
                            Text("Registrar progreso")
                        }

                        IconButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Quitar de mi lista",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
                    )
                    Text(
                        text = "Descripción del libro:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    // La descripción puede contener etiquetas HTML y se debe convertir a texto plano
                    val description = parseHtmlToText(book.description?: "Sin descripción")
                    Text(
                        text = description,
                        fontSize = 12.sp
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
                                    } else if (pages > book.totalPages) {
                                        inputError = "No puede superar ${book.totalPages} páginas"
                                    } else {
                                        val statusInitial = if (pages == 0) Constants.TO_READ else Constants.IN_PROGRESS
                                        val newStatus = if (pages == book.totalPages) Constants.COMPLETED else statusInitial
                                        viewModel.updateProgressAndStatus(book.idGoogle!!, pages, newStatus)
                                        showDialog = false
                                        Toast.makeText(
                                            context,
                                            "Se ha actualizado su progreso",
                                            Toast.LENGTH_LONG
                                        ).show()
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

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Quitar de mi lista") },
                        text = { Text("¿Está seguro de quitar este libro de su lista?") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.deleteBook(book.idGoogle ?: "")
                                showDeleteDialog = false
                                navController.popBackStack()
                                Toast.makeText(
                                    context,
                                    "Se ha quitado de su lista",
                                    Toast.LENGTH_LONG
                                ).show()
                            }) {
                                Text("Quitar", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}
