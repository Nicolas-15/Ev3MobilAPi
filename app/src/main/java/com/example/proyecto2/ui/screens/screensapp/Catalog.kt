// app/src/main/java/com/example/proyecto2/ui/screens/CatalogScreen.kt
package com.example.proyecto2.ui.screens.screensapp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.proyecto2.MainActivity
import com.example.proyecto2.data.model.Producto
import com.example.proyecto2.navigation.AppNavigations
import com.example.proyecto2.ui.screens.home.HomeScreenCompacta
import com.example.proyecto2.ui.screens.home.HomeScreenMediana

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    activity: MainActivity
) {
    val windowSizeClass = calculateWindowSizeClass(activity = activity)

    val onProductClicked = { producto: Producto ->
        navController.navigate("${AppNavigations.PRODUCT_DETAIL_ROUTE}/${producto.id}")
    }

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            HomeScreenCompacta(
                navController = navController,
                onProductClicked = onProductClicked
            )
        }
        else -> {
            HomeScreenMediana(
                navController = navController,
                onProductClicked = onProductClicked
            )
        }
    }
}