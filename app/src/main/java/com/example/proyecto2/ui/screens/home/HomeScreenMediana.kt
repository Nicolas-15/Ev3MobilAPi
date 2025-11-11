// Contenido para: app/src/main/java/com/example/proyecto2/ui/screens/HomeScreenMediana.kt

package com.example.proyecto2.ui.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyecto2.model.Producto
import com.example.proyecto2.ui.screens.ContenidoPrincipal

@Composable
fun HomeScreenMediana(
    navController: NavController,
    onProductClicked: (Producto) -> Unit // <-- Recibe la acción de clic
) {
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            NavigationRailItem(
                selected = true,
                onClick = { /* TODO */ },
                icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") }
            )
            NavigationRailItem(
                selected = false,
                onClick = { /* TODO */ },
                icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                label = { Text("Buscar") }
            )
            NavigationRailItem(
                selected = false,
                onClick = { /* TODO */ },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
                label = { Text("Ajustes") }
            )
        }
        // ¡CORRECCIÓN CLAVE! Pasa la acción a ContenidoPrincipal
        ContenidoPrincipal(onProductClicked = onProductClicked)
    }
}

@Preview(showSystemUi = true, device = "spec:width=673.5dp,height=841dp,dpi=480")
@Composable
fun PreviewHomeScreenMediana() {
    HomeScreenMediana(
        navController = rememberNavController(),
        onProductClicked = {}
    )
}
