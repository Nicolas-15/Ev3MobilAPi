// Contenido para: app/src/main/java/com/example/proyecto2/ui/screens/HomeScreenCompacta.kt

package com.example.proyecto2.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyecto2.data.model.Producto
import com.example.proyecto2.ui.screens.screensapp.ContenidoPrincipal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(
    navController: NavController,
    onProductClicked: (Producto) -> Unit // <-- Recibe la acción de clic
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()){
                        Text("Catálogo", modifier = Modifier.align(Alignment.Center))
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                IconButton(onClick = { /* TODO */ }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Home, contentDescription = "Inicio")
                }
                IconButton(onClick = { /* TODO */ }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
                IconButton(onClick = { /* TODO */ }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                }
            }
        }
    ) { innerPadding ->
        ContenidoPrincipal(
            modifier = Modifier.padding(innerPadding),
            onProductClicked = onProductClicked
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewHomeScreenCompacta() {
    HomeScreenCompacta(
        navController = rememberNavController(),
        onProductClicked = {}
    )
}
