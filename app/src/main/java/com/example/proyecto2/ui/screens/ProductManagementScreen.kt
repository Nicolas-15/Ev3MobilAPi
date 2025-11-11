// Contenido para: app/src/main/java/com/example/proyecto2/ui/screens/ProductManagementScreen.kt

@file:Suppress("DEPRECATION")

package com.example.proyecto2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyecto2.model.Producto
import com.example.proyecto2.ui.viewmodels.AdminViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagementScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val productos = uiState.filteredProducts

    var mostrarDialogoAdd by remember { mutableStateOf(false) }
    var mostrarDialogoEdit by remember { mutableStateOf<Producto?>(null) }

    // 1. Añadimos el estado para el Snackbar y el scope para lanzarlo.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        // 2. Conectamos el Snackbar al Scaffold.
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Propiedades") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogoAdd = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Propiedad")
            }
        }
    ) { paddingValues ->
        if (mostrarDialogoAdd) {
            AddProductDialog(
                onDismiss = { mostrarDialogoAdd = false },
                onConfirm = { nombre, descripcion, precio, categoria ->
                    viewModel.agregarProducto(nombre, descripcion, precio, categoria)
                    mostrarDialogoAdd = false
                    // 3. Mostramos el Snackbar al confirmar.
                    scope.launch {
                        snackbarHostState.showSnackbar("Propiedad añadida correctamente")
                    }
                }
            )
        }

        mostrarDialogoEdit?.let { producto ->
            EditProductDialog(
                producto = producto,
                onDismiss = { mostrarDialogoEdit = null },
                onConfirm = { productoActualizado ->
                    viewModel.actualizarProducto(productoActualizado)
                    mostrarDialogoEdit = null
                    // 3. Mostramos el Snackbar al confirmar.
                    scope.launch {
                        snackbarHostState.showSnackbar("Propiedad actualizada correctamente")
                    }
                }
            )
        }

        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = uiState.searchTerm,
                onValueChange = viewModel::onSearchTermChange,
                label = { Text("Buscar por nombre o descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(productos) { producto ->
                    AdminProductCard(
                        producto = producto,
                        onDeleteClick = {
                            viewModel.eliminarProducto(producto)
                            // 3. Mostramos el Snackbar al eliminar.
                            scope.launch {
                                snackbarHostState.showSnackbar("Propiedad eliminada")
                            }
                        },
                        onEditClick = { mostrarDialogoEdit = producto }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminProductCard(
    producto: Producto,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleLarge)
                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                Text(
                    text = format.format(producto.precio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    // Estados para los mensajes de error de validación.
    var nombreError by remember { mutableStateOf<String?>(null) }
    var precioError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        nombreError = if (nombre.isBlank()) "El nombre no puede estar vacío" else null
        val precioDouble = precio.toDoubleOrNull()
        precioError = if (precioDouble == null || precioDouble <= 0) "El precio debe ser un número positivo" else null
        return nombreError == null && precioError == null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Nueva Propiedad") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it; nombreError = null },
                    label = { Text("Nombre") },
                    isError = nombreError != null,
                    supportingText = { if (nombreError != null) Text(nombreError!!) }
                )
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it; precioError = null },
                    label = { Text("Precio (CLP)") },
                    isError = precioError != null,
                    supportingText = { if (precioError != null) Text(precioError!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (validate()) {
                        onConfirm(nombre, descripcion, precio.toDouble(), categoria)
                    }
                }
            ) { Text("Añadir") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun EditProductDialog(
    producto: Producto,
    onDismiss: () -> Unit,
    onConfirm: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(producto.nombre) }
    var descripcion by remember { mutableStateOf(producto.descripcion) }
    var precio by remember { mutableStateOf(producto.precio.toString()) }
    var categoria by remember { mutableStateOf(producto.categoria) }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var precioError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        nombreError = if (nombre.isBlank()) "El nombre no puede estar vacío" else null
        val precioDouble = precio.toDoubleOrNull()
        precioError = if (precioDouble == null || precioDouble <= 0) "El precio debe ser un número positivo" else null
        return nombreError == null && precioError == null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Propiedad") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it; nombreError = null },
                    label = { Text("Nombre") },
                    isError = nombreError != null,
                    supportingText = { if (nombreError != null) Text(nombreError!!) }
                )
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it; precioError = null },
                    label = { Text("Precio (CLP)") },
                    isError = precioError != null,
                    supportingText = { if (precioError != null) Text(precioError!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (validate()) {
                        val productoActualizado = producto.copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            precio = precio.toDouble(),
                            categoria = categoria
                        )
                        onConfirm(productoActualizado)
                    }
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
