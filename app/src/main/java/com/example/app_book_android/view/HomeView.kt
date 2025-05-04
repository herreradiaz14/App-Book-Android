package com.example.app_book_android.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.app_book_android.model.Book
import com.example.app_book_android.utils.convertSecureURL
import com.example.app_book_android.viewmodel.HomeViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.app_book_android.ui.theme.PurplePrimary
import com.example.app_book_android.ui.theme.WhiteCard
import com.example.app_book_android.utils.BookStatus
import com.example.app_book_android.utils.Constants
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp


@Composable
fun Home(viewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {
    val books by viewModel.books.collectAsState()
    val tabTitles = listOf("Por leer", "Leídos")
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 110.dp, bottom = 25.dp)) {
        TabRow(
            selectedTabIndex = selectedTab,
            contentColor = PurplePrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .horizontalScroll(rememberScrollState())
                        .padding(start = 30.dp, end = 30.dp, top = 30.dp),
                    color = PurplePrimary
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = if (index == 0) Icons.Outlined.Menu else Icons.Outlined.CheckCircle,
                                contentDescription = title,
                                tint = if (selectedTab == index) PurplePrimary else Color.Gray
                            )
                            Text(
                                text = title.uppercase(),
                                color = if (selectedTab == index) PurplePrimary else Color.Gray
                            )
                        }
                    }
                )
            }
        }
        when(selectedTab) {
            0 -> {
                BookTab(
                    books = books.filter { it.status != Constants.COMPLETED },
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp),
                    navController = navController
                )
            }
            1 -> {
                BookTab(
                    books = books.filter { it.status == Constants.COMPLETED },
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp),
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun BookTab(
    books: List<Book>,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    if (books.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay libros para mostrar.", fontSize = 16.sp)
        }
    } else {
        LazyColumn(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp, bottom = 8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(books) { book ->
                BookRow(book = book, viewBookDetailClick = {
                    viewModel.viewBookDetail(navController, book.idGoogle.toString())
                })
            }
        }
    }
}

@Composable
fun BookRow(book: Book, viewBookDetailClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteCard),
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .clickable { viewBookDetailClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                val thumbnail = book.thumbnail.toString()
                BookImage(thumbnail = thumbnail, thumbnailHeight = 100.dp)
                Spacer(modifier = Modifier.width(20.dp))

                val statusEnum = BookStatus.fromKey(book.status ?: Constants.TO_READ)
                Column(modifier = Modifier.weight(1f)) {
                    Text(book.title.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Autor: ${book.author}", fontSize = 14.sp)
                    Text("Estado: ${statusEnum.displayName}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    ProgressIndicator(book)
                }
            }
        }
    }
}

@Composable
fun ProgressIndicator(book: Book) {
    val progress = if (book.totalPages > 0) book.currentPage.toFloat() / book.totalPages else 0f

    Column {
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = PurplePrimary,
            strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text("${book.currentPage} de ${book.totalPages} páginas", fontSize = 13.sp)
    }
}

@Composable
fun BookImage(thumbnail: String, thumbnailHeight: Dp) {
    if (thumbnail.isBlank()){
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
            contentDescription = null,
            modifier = Modifier.height(thumbnailHeight)
        )
    }else{
        AsyncImage(
            model = convertSecureURL(thumbnail),
            contentDescription = null,
            modifier = Modifier.height(thumbnailHeight)
        )
    }
}