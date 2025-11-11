// Contenido COMPLETO para: app/src/main/java/com/example/proyecto2/navigation/AppNavigation.kt

package com.example.proyecto2.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.proyecto2.MainActivity

// --- Mis Imports para las pantallas y ViewModels ---
import com.example.proyecto2.ui.screens.AdminLandingScreen
import com.example.proyecto2.ui.screens.CatalogScreen
import com.example.proyecto2.ui.screens.ClientHomeScreen
import com.example.proyecto2.ui.screens.MyOrdersScreen
import com.example.proyecto2.ui.screens.ProductDetailScreen
import com.example.proyecto2.ui.screens.ProductManagementScreen // <-- Import para mi nueva pantalla de admin
import com.example.proyecto2.ui.screens.auth.LoginScreen
import com.example.proyecto2.ui.screens.auth.RegisterScreen
import com.example.proyecto2.ui.viewmodels.AdminViewModel // <-- Import para mi nuevo ViewModel de admin
import com.example.proyecto2.ui.viewmodels.ClientViewModel

object AppNavigations{
    // Rutas de autenticación
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"

    // Rutas principales post-login
    const val ADMIN_LANDING_ROUTE = "admin_landing"
    const val CLIENT_GRAPH_ROUTE = "client_graph"

    // Sub-rutas de Cliente
    const val CLIENT_HOME_ROUTE = "client_home"
    const val CATALOG_ROUTE = "client_catalog"
    const val MY_ORDERS_ROUTE = "client_my_orders"
    const val PRODUCT_DETAIL_ROUTE = "product_detail"

    // Sub-rutas de Admin
    const val PRODUCT_ROUTE = "admin_products"
    const val INVENTORY_ROUTE = "admin_inventory"
    const val ORDERS_ROUTE = "admin_orders"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    activity: MainActivity,
    startDestination: String = AppNavigations.LOGIN_ROUTE
) {
    // --- 1. Aquí creo las instancias de MIS ViewModels ---
    // El ciclo de vida de estos ViewModels está ligado a AppNavHost,
    // por lo que persisten mientras la app esté abierta y puedo compartirlos.
    val clientViewModel: ClientViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel() // <-- Aquí instancio el de admin

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- NAVEGACIÓN DE AUTENTICACIÓN (Sin cambios) ---
        composable(AppNavigations.LOGIN_ROUTE) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(AppNavigations.REGISTER_ROUTE)
                },
                onLoginSuccessAsAdmin = {
                    navController.navigate(AppNavigations.ADMIN_LANDING_ROUTE) {
                        popUpTo(AppNavigations.LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onLoginSuccessAsClient = {
                    navController.navigate(AppNavigations.CLIENT_GRAPH_ROUTE) {
                        popUpTo(AppNavigations.LOGIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(AppNavigations.REGISTER_ROUTE) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateBackToLogin = { navController.popBackStack() }
            )
        }

        // --- NAVEGACIÓN DE ADMINISTRADOR ---
        composable(AppNavigations.ADMIN_LANDING_ROUTE) {
            AdminLandingScreen(
                onNavigateToProducts = { navController.navigate(AppNavigations.PRODUCT_ROUTE) },
                onNavigateToInventory = { navController.navigate(AppNavigations.INVENTORY_ROUTE) },
                onNavigateToOrders = { navController.navigate(AppNavigations.ORDERS_ROUTE) },
                onNavigateToRoleSelection = {
                    navController.navigate(AppNavigations.LOGIN_ROUTE) {
                        popUpTo(AppNavigations.ADMIN_LANDING_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // --- 2. AQUÍ ESTÁ EL CAMBIO IMPORTANTE ---
        // He reemplazado el PlaceholderScreen por mi nueva pantalla de gestión.
        composable(AppNavigations.PRODUCT_ROUTE) {
            ProductManagementScreen(
                viewModel = adminViewModel, // Le paso el ViewModel que he creado arriba
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- GRAFO DE NAVEGACIÓN ANIDADO PARA CLIENTE (Sin cambios) ---
        navigation(
            startDestination = AppNavigations.CLIENT_HOME_ROUTE,
            route = AppNavigations.CLIENT_GRAPH_ROUTE
        ) {
            composable(AppNavigations.CLIENT_HOME_ROUTE) {
                ClientHomeScreen(
                    onNavigateToCatalog = { navController.navigate(AppNavigations.CATALOG_ROUTE) },
                    onNavigateToMyOrders = { navController.navigate(AppNavigations.MY_ORDERS_ROUTE) },
                    onNavigateToRoleSelection = {
                        navController.navigate(AppNavigations.LOGIN_ROUTE) {
                            popUpTo(AppNavigations.CLIENT_GRAPH_ROUTE) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppNavigations.CATALOG_ROUTE) {
                CatalogScreen(navController = navController, activity = activity)
            }
            composable(
                route = "${AppNavigations.PRODUCT_DETAIL_ROUTE}/{productoId}",
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getInt("productoId")
                if (productoId != null) {
                    ProductDetailScreen(
                        productoId = productoId,
                        viewModel = clientViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                } else {
                    navController.popBackStack()
                }
            }
            composable(AppNavigations.MY_ORDERS_ROUTE) {
                MyOrdersScreen(
                    viewModel = clientViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // --- Mis otras pantallas Placeholder de Admin (se quedan así por ahora) ---
        composable(AppNavigations.INVENTORY_ROUTE) {
            PlaceholderScreen(screenName = "Gestión de Inventario") { navController.popBackStack() }
        }
        composable(AppNavigations.ORDERS_ROUTE) {
            PlaceholderScreen(screenName = "Gestión de Pedidos") { navController.popBackStack() }
        }
    }
}

// Mi Composable genérico para las pantallas no implementadas (sin cambios)
@Composable
fun PlaceholderScreen(screenName: String, onNavigate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Pantalla de:", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = screenName, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigate) {
                Text("Volver")
            }
        }
    }
}
