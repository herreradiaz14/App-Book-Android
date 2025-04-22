package com.example.app_book_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.app_book_android.ui.theme.AppBookAndroidTheme
import com.example.app_book_android.view.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppBookAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    MainScreen()
                }
            }
        }
    }
}
//https://www.youtube.com/watch?v=eSIGmU2k2CA&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl
//https://www.googleapis.com/books/v1/volumes/abYKXvCwEToC
//https://www.postman.com/postman/commerce-api/request/fnlo0jg/retrieve-volume