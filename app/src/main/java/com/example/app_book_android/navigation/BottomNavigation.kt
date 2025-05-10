package com.example.app_book_android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigation (
    val route: String,
    val unselectedIcon: ImageVector? = null,
    val selectedIcon: ImageVector? = null,
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
        unselectedIcon = Icons.Outlined.Add,
        selectedIcon = Icons.Filled.AddCircle,
        title = "Agregar"
    )
    data object Notification: BottomNavigation(
        route = "notification",
        unselectedIcon = Icons.Outlined.Notifications,
        selectedIcon = Icons.Filled.Notifications,
        title = "Notificac."
    )
    data object Profile: BottomNavigation(
        route = "profile",
        unselectedIcon = Icons.Outlined.AccountCircle,
        selectedIcon = Icons.Filled.AccountCircle,
        title = "Perfil"
    )
    data object BookDetail: BottomNavigation(
        route = "book_detail",
        title = "Detalle del libro"
    )
}