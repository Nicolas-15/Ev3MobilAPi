// Contenido para: app/src/main/java/com/example/proyecto2/ui/viewmodels/AdminViewModel.kt

package com.example.proyecto2.viewmodels

import androidx.lifecycle.ViewModel
import com.example.proyecto2.data.FakeProductDataSource
import com.example.proyecto2.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Defino el estado de la UI para mi panel de administración.
data class AdminUiState(
    // La lista de todos los productos que gestionará el admin.
    val productos: List<Producto> = FakeProductDataSource.productos,
    // El término de búsqueda actual.
    val searchTerm: String = ""
) {
    // Propiedad computada que devuelve la lista de productos filtrada según el searchTerm.
    val filteredProducts: List<Producto> get() {
        return if (searchTerm.isBlank()) {
            productos
        } else {
            // Filtro por nombre o descripción, ignorando mayúsculas/minúsculas.
            productos.filter {
                it.nombre.contains(searchTerm, ignoreCase = true) ||
                it.descripcion.contains(searchTerm, ignoreCase = true)
            }
        }
    }
}


class AdminViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState = _uiState.asStateFlow()

    // Función para actualizar el término de búsqueda en el estado.
    fun onSearchTermChange(newTerm: String) {
        _uiState.update { it.copy(searchTerm = newTerm) }
    }

    // Mi función para añadir una nueva propiedad.
    fun agregarProducto(nombre: String, descripcion: String, precio: Double, categoria: String) {
        // Para el nuevo ID, busco el ID más alto de la lista actual y le sumo 1.
        // Así me aseguro de que cada ID sea único.
        val nuevoId = (_uiState.value.productos.maxOfOrNull { it.id } ?: 0) + 1

        // Creo el objeto del nuevo producto.
        val nuevoProducto = Producto(
            id = nuevoId,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            categoria = categoria,
            // ¡CORRECCIÓN! Usamos "imagen" (String) en lugar de "imagenId" (Int).
            // Le ponemos una URL de placeholder mientras no tengamos un selector de imágenes real.
            imagen = "https://via.placeholder.com/400x300.png?text=Nueva+Propiedad"
        )

        // Actualizo el estado de mi UI, añadiendo el nuevo producto a la lista.
        _uiState.update { currentState ->
            currentState.copy(productos = currentState.productos + nuevoProducto)
        }
    }

    // Mi función para eliminar una propiedad.
    fun eliminarProducto(producto: Producto) {
        _uiState.update { currentState ->
            // Para eliminar, simplemente filtro la lista y creo una nueva
            // que contenga todos los productos EXCEPTO el que quiero quitar.
            val listaActualizada = currentState.productos.filter { it.id != producto.id }
            currentState.copy(productos = listaActualizada)
        }
    }

    // Mi función para actualizar una propiedad existente.
    fun actualizarProducto(productoActualizado: Producto) {
        _uiState.update { currentState ->
            val listaActualizada = currentState.productos.map {
                if (it.id == productoActualizado.id) {
                    productoActualizado
                } else {
                    it
                }
            }
            currentState.copy(productos = listaActualizada)
        }
    }
}
