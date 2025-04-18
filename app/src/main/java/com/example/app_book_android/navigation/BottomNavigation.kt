package com.example.app_book_android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigation (
    val route: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val title: String,
){
    data object Home: BottomNavigation(
        route = "home",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        title = "Inicio"
    )
    data object Search: BottomNavigation(
        route = "search",
        unselectedIcon = Icons.Outlined.Search,
        selectedIcon = Icons.Filled.Search,
        title = "Buscar"
    )
    data object Notification: BottomNavigation(
        route = "notification",
        unselectedIcon = Icons.Outlined.Notifications,
        selectedIcon = Icons.Filled.Notifications,
        title = "Notificaciones"
    )
}