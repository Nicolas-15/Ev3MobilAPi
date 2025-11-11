package com.example.proyecto2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.proyecto2.ui.screens.Api.PostScreen
import com.example.proyecto2.ui.theme.Proyecto2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto2Theme {
                PostScreen()
            }
        }
    }
}
