package com.example.proyecto2.navigation

import androidx.compose.foundation.layout.*
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
import com.example.proyecto2.ui.screens.auth.LoginScreen
import com.example.proyecto2.ui.screens.auth.RegisterScreen
import com.example.proyecto2.ui.screens.screensapp.*
import com.example.proyecto2.viewmodels.AdminViewModel
import com.example.proyecto2.viewmodels.ClientViewModel

/**
 * Objeto que centraliza todas las rutas de navegación de la aplicación.
 * Usar un objeto como este previene errores de tipeo en las rutas y facilita su mantenimiento.
 */
object AppNavigations {
    // Rutas de autenticación
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"

    // Rutas principales post-login
    const val ADMIN_LANDING_ROUTE = "admin_landing"
    const val CLIENT_GRAPH_ROUTE = "client_graph" // Ruta para el grafo de navegación del cliente

    // Sub-rutas del grafo de Cliente
    const val CLIENT_HOME_ROUTE = "client_home"
    const val CATALOG_ROUTE = "client_catalog"
    const val MY_ORDERS_ROUTE = "client_my_orders"
    const val PRODUCT_DETAIL_ROUTE = "product_detail"

    // Sub-rutas de Admin
    const val PRODUCT_ROUTE = "admin_products"
    const val INVENTORY_ROUTE = "admin_inventory"
    const val ORDERS_ROUTE = "admin_orders"
}

/**
 * `AppNavHost` es el Composable central que gestiona la navegación de toda la aplicación.
 * Define qué pantalla (`Composable`) se muestra para cada ruta.
 *
 * @param navController El controlador que gestiona el estado de la navegación (la pila de pantallas).
 * @param activity La instancia de `MainActivity`, necesaria para algunas funcionalidades como `calculateWindowSizeClass`.
 * @param startDestination La ruta inicial que se mostrará al lanzar la aplicación.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    activity: MainActivity,
    startDestination: String = AppNavigations.LOGIN_ROUTE
) {
    // Creación de las instancias de los ViewModels.
    // Al crearlos aquí con `viewModel()`, su ciclo de vida se asocia al del `AppNavHost`.
    // Esto permite que un mismo ViewModel sea compartido por varias pantallas, manteniendo el estado.
    val clientViewModel: ClientViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- NAVEGACIÓN DE AUTENTICACIÓN ---
        composable(AppNavigations.LOGIN_ROUTE) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(AppNavigations.REGISTER_ROUTE) },
                onLoginSuccessAsAdmin = {
                    // Navega a la pantalla de admin y limpia la pila de navegación anterior.
                    navController.navigate(AppNavigations.ADMIN_LANDING_ROUTE) {
                        popUpTo(AppNavigations.LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onLoginSuccessAsClient = {
                    // Navega al grafo de cliente y limpia la pila de navegación anterior.
                    navController.navigate(AppNavigations.CLIENT_GRAPH_ROUTE) {
                        popUpTo(AppNavigations.LOGIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(AppNavigations.REGISTER_ROUTE) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() }, // Vuelve a Login después de un registro exitoso.
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
                    // Cierra sesión y vuelve a la pantalla de Login.
                    navController.navigate(AppNavigations.LOGIN_ROUTE) {
                        popUpTo(AppNavigations.ADMIN_LANDING_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(AppNavigations.PRODUCT_ROUTE) {
            ProductManagementScreen(
                viewModel = adminViewModel, // Comparte el ViewModel de Admin.
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- GRAFO DE NAVEGACIÓN ANIDADO PARA CLIENTE ---
        // Agrupa todas las pantallas relacionadas con el cliente bajo una misma ruta (`CLIENT_GRAPH_ROUTE`).
        // Esto ayuda a organizar el código y a manejar la pila de navegación de forma más limpia.
        navigation(
            startDestination = AppNavigations.CLIENT_HOME_ROUTE, // La primera pantalla del grafo de cliente.
            route = AppNavigations.CLIENT_GRAPH_ROUTE
        ) {
            composable(AppNavigations.CLIENT_HOME_ROUTE) {
                ClientHomeScreen(
                    onNavigateToCatalog = { navController.navigate(AppNavigations.CATALOG_ROUTE) },
                    onNavigateToMyOrders = { navController.navigate(AppNavigations.MY_ORDERS_ROUTE) },
                    onNavigateToRoleSelection = {
                        // Cierra sesión y vuelve a la pantalla de Login.
                        navController.navigate(AppNavigations.LOGIN_ROUTE) {
                            popUpTo(AppNavigations.CLIENT_GRAPH_ROUTE) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppNavigations.CATALOG_ROUTE) {
                // Llama a la pantalla que decide qué layout mostrar (compacto o mediano).
                CatalogScreen(
                    navController = navController,
                    activity = activity
                )
            }
            composable(
                route = "${AppNavigations.PRODUCT_DETAIL_ROUTE}/{productoId}", // Define una ruta con un argumento dinámico.
                arguments = listOf(navArgument("productoId") { type = NavType.IntType }) // Especifica el tipo del argumento.
            ) { backStackEntry ->
                // Extrae el ID del producto de los argumentos de la ruta.
                val productoId = backStackEntry.arguments?.getInt("productoId")
                if (productoId != null) {
                    ProductDetailScreen(
                        productoId = productoId,
                        viewModel = clientViewModel, // Comparte el ViewModel de Cliente.
                        onNavigateBack = { navController.popBackStack() },
                        // Define la acción para el botón "VER LISTA" del Snackbar.
                        onNavigateToMyOrders = { navController.navigate(AppNavigations.MY_ORDERS_ROUTE) }
                    )
                } else {
                    // Si el ID no es válido, simplemente vuelve a la pantalla anterior.
                    navController.popBackStack()
                }
            }
            composable(AppNavigations.MY_ORDERS_ROUTE) {
                MyOrdersScreen(
                    viewModel = clientViewModel, // Comparte el ViewModel de Cliente.
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // --- Pantallas Placeholder de Admin (sin implementar) ---
        composable(AppNavigations.INVENTORY_ROUTE) {
            PlaceholderScreen(screenName = "Gestión de Inventario") { navController.popBackStack() }
        }
        composable(AppNavigations.ORDERS_ROUTE) {
            PlaceholderScreen(screenName = "Gestión de Pedidos") { navController.popBackStack() }
        }
    }
}

/**
 * Composable genérico para mostrar en pantallas que aún no han sido implementadas.
 * @param screenName El nombre de la pantalla a mostrar.
 * @param onNavigate Lambda para el botón de "Volver".
 */
@Composable
fun PlaceholderScreen(screenName: String, onNavigate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
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
