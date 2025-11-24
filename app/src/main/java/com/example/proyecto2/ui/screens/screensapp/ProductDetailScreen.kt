package com.example.proyecto2.ui.screens.screensapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.proyecto2.viewmodels.ClientViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

/**
 * Muestra la pantalla de detalle para un producto específico.
 * Esta pantalla es responsable de:
 * 1.  Recuperar el producto seleccionado a través del `ClientViewModel`.
 * 2.  Mostrar toda la información del producto (imagen, nombre, precio, descripción).
 * 3.  Proporcionar un botón "Me interesa" que añade el producto a una lista de favoritos/arriendos.
 * 4.  Mostrar un `Snackbar` con una acción para navegar a "Mis Arriendos" después de añadir un producto.
 *
 * @param productoId El ID del producto que se debe mostrar.
 * @param viewModel La instancia de `ClientViewModel` para acceder a la lógica de negocio (encontrar producto, añadir a favoritos).
 * @param onNavigateBack Lambda para manejar la acción de volver a la pantalla anterior.
 * @param onNavigateToMyOrders Lambda que se invoca cuando el usuario presiona la acción "VER LISTA" en el Snackbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productoId: Int,
    viewModel: ClientViewModel, // ViewModel compartido para la lógica de cliente.
    onNavigateBack: () -> Unit, // Función para navegar hacia atrás.
    onNavigateToMyOrders: () -> Unit // Función para navegar a la pantalla de "Mis Arriendos".
) {
    // Se busca el producto en la lista del ViewModel usando el ID recibido.
    val producto = viewModel.findProductoById(productoId)

    // `snackbarHostState` controla la visualización de los Snackbars.
    val snackbarHostState = remember { SnackbarHostState() }
    // `scope` es necesario para lanzar el Snackbar en una corrutina.
    val scope = rememberCoroutineScope()

    // `Scaffold` proporciona la estructura básica de la pantalla (barra superior, contenido, etc.).
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, // Contenedor para los Snackbars.
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Detalle del Producto") }, // Título dinámico con el nombre del producto.
                navigationIcon = {
                    // Icono de flecha para volver atrás.
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Si por alguna razón el producto no se encuentra, muestra un mensaje de error.
        if (producto == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Producto no encontrado.")
            }
            return@Scaffold // Termina la ejecución del Composable aquí.
        }

        // Columna principal que contiene toda la información del producto.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplica el padding del Scaffold.
                .padding(16.dp) // Padding adicional para el contenido.
                .verticalScroll(rememberScrollState()) // Permite el desplazamiento vertical si el contenido no cabe.
        ) {
            // Carga la imagen del producto desde la URL.
            AsyncImage(
                model = producto.imagen,
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop // Recorta la imagen para que llene el espacio.
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre del producto.
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Formatea el precio a moneda local (Peso Chileno).
            val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            Text(
                text = format.format(producto.precio),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción del producto.
            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyLarge
            )

            // Spacer con `weight` para empujar el botón hacia el fondo de la pantalla.
            Spacer(modifier = Modifier.weight(1f))

            // Botón "Me interesa".
            Button(
                onClick = {
                    // Añade el producto a la lista de favoritos en el ViewModel.
                    viewModel.agregarAFavoritos(producto)
                    // Lanza una corrutina para mostrar el Snackbar sin bloquear el hilo principal.
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Añadido a 'Mis Arriendos'",
                            actionLabel = "VER LISTA",
                            duration = SnackbarDuration.Short
                        )
                        // Si el usuario presiona la acción "VER LISTA", navega a la pantalla correspondiente.
                        if (result == SnackbarResult.ActionPerformed) {
                            onNavigateToMyOrders()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Me interesa")
            }
        }
    }
}
