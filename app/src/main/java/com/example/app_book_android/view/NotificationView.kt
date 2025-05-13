package com.example.app_book_android.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_book_android.model.NotificationBook
import com.example.app_book_android.ui.theme.PurplePrimary
import com.example.app_book_android.ui.theme.WhiteCard
import com.example.app_book_android.utils.Constants
import com.example.app_book_android.viewmodel.NotificationViewModel
import com.example.app_book_android.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun Notification(viewModel: NotificationViewModel = hiltViewModel()) {
    val notifications by viewModel.notifications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    val profileViewModel: ProfileViewModel = hiltViewModel()
    val user by profileViewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.checkCurrentUser()
    }

    LaunchedEffect(user) {
        if (user != null) {
            viewModel.loadNotifications(user?.id ?: Constants.GUEST)
        } else {
            viewModel.loadNotifications(Constants.GUEST)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 100.dp, bottom = 25.dp, start = 30.dp, end = 30.dp)
    ) {
        when {
            isLoading -> {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = PurplePrimary)
                }
            }
            notifications.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null
                    )
                    Text(
                        text = "No hay notificaciones",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            else -> {
                LazyColumn {
                    items(notifications) { notification ->
                        NotificationItem(notification)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationBook) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteCard),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(notification.title, fontWeight = FontWeight.Bold)
            Text(notification.body)
            notification.timestamp?.let {
                Text(
                    text = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm", Locale.getDefault()).format(it.toDate()
                    ),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
