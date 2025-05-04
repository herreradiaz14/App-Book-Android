package com.example.app_book_android.view

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app_book_android.navigation.BottomNavigation
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomTabBar(navController) }
    ) {
        NavigationGraph(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavigation.Home.route) {
        composable(BottomNavigation.Home.route) {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Mis Libros") }) },
                content = { Home(navController = navController) }
            )
        }
        composable(BottomNavigation.Search.route) {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Agregar libro") }) },
                content = { Search() }
            )
        }
        composable(BottomNavigation.Notification.route) {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Notificaciones") }) },
                content = { Notification() }
            )
        }

        composable(
            route = "${BottomNavigation.BookDetail}/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            if (bookId != null) {
                BookDetail(bookId = bookId, navController = navController)
            }
        }
    }
}


@Composable
fun BottomTabBar(navController: NavHostController) {
    val items = listOf(
        BottomNavigation.Home,
        BottomNavigation.Search,
        BottomNavigation.Notification,
    )

    BottomAppBar {
        val navBackStack by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStack?.destination?.route

        items.forEach { barItem ->
            val selected = barItem.route == currentRoute

            NavigationBarItem(
                selected = selected,
                label = { Text(barItem.title) },
                onClick = {
                    navController.navigate(barItem.route) {
                        navController.graph.startDestinationRoute.let { route ->
                            if (route != null) {
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) barItem.selectedIcon!! else barItem.unselectedIcon!!,
                        contentDescription = null
                    )
                }
            )
        }
    }
}