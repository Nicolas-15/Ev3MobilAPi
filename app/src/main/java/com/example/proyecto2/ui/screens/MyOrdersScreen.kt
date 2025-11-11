// Contenido COMPLETO para: app/src/main/java/com/example/proyecto2/ui/screens/MyOrdersScreen.kt

package com.example.proyecto2.ui.screens

// Mis imports necesarios para la pantalla
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete // Importo el ícono de la papelera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto2.model.Producto // Importo mi modelo de datos
import com.example.proyecto2.ui.viewmodels.ClientViewModel // Importo el ViewModel que controla la lógica

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(
    viewModel: ClientViewModel,
    onNavigateBack: () -> Unit
) {
    // 1. Primero, observo el estado del ViewModel para obtener la lista de favoritos.
    //    Gracias a collectAsState, la pantalla se actualizará sola si la lista cambia.
    val uiState by viewModel.uiState.collectAsState()
    val favoritos = uiState.favoritos

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Arriendos de Interés") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        // 2. Compruebo si la lista de favoritos está vacía.
        if (favoritos.isEmpty()) {
            // Si está vacía, muestro un mensaje amigable al usuario.
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no has mostrado interés en ningún arriendo.")
            }
        } else {
            // 3. Si hay favoritos, creo una lista vertical (LazyColumn).
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Por cada 'producto' en mi lista de 'favoritos', creo una 'FavoritoCard'.
                items(favoritos) { producto ->
                    FavoritoCard(
                        producto = producto,
                        // La acción 'onRemoveClick' de la tarjeta llamará a mi ViewModel.
                        onRemoveClick = {
                            // Le digo al ViewModel que elimine este producto específico.
                            viewModel.removerDeFavoritos(it)
                        }
                    )
                }
            }
        }
    }
}

// 4. He creado este nuevo Composable para mostrar cada elemento de la lista de favoritos.
//    Así mantengo la pantalla 'MyOrdersScreen' más limpia y organizada.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritoCard(
    producto: Producto,
    onRemoveClick: (Producto) -> Unit // La tarjeta recibe una función para manejar el clic de eliminar.
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Alinear verticalmente los elementos.
        ) {
            // Un 'Box' que ocupa la mayor parte del espacio para mostrar la información del producto.
            Box(modifier = Modifier.weight(1f)) {
                // Reutilizo la 'ProductoCard' que ya había creado.
                // Le paso un 'onClick' vacío porque no quiero que navegue a ningún lado desde aquí.
                ProductoCard(
                    producto = producto,
                    modifier = Modifier.fillMaxWidth(),
                    onProductClicked = {}
                )
            }

            // El botón para eliminar, que ocupa el espacio restante.
            IconButton(
                onClick = { onRemoveClick(producto) }, // Al hacer clic, se ejecuta la función que me pasaron.
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar de favoritos",
                    // Le doy un color rojo para que el usuario entienda que es una acción destructiva.
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
