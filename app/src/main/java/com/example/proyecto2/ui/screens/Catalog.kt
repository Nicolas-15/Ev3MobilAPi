// Contenido para: app/src/main/java/com/example/proyecto2/ui/screens/CatalogScreen.kt

package com.example.proyecto2.ui.screens

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.proyecto2.MainActivity // Asegúrate que el import sea correcto
import com.example.proyecto2.model.Producto
import com.example.proyecto2.navigation.AppNavigations
import com.example.proyecto2.ui.screens.home.HomeScreenCompacta
import com.example.proyecto2.ui.screens.home.HomeScreenMediana

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun CatalogScreen(navController: NavController, activity: MainActivity) {
    // Calcula el tamaño de la ventana para decidir qué layout usar
    val windowSizeClass = calculateWindowSizeClass(activity = activity)

    // Define la acción de navegación UNA SOLA VEZ
    val onProductClickedAction = { producto: Producto ->
        navController.navigate("${AppNavigations.PRODUCT_DETAIL_ROUTE}/${producto.id}")
    }

    // Decide qué layout mostrar basado en el ancho de la pantalla
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            HomeScreenCompacta(
                navController = navController,
                onProductClicked = onProductClickedAction // Pasa la acción
            )
        }
        else -> { // Para Medium y Expanded
            HomeScreenMediana(
                navController = navController,
                onProductClicked = onProductClickedAction // Pasa la acción
            )
        }
    }
}


