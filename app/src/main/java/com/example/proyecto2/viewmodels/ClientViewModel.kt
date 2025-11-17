// En: app/src/main/java/com/example/proyecto2/ui/viewmodels/ClientViewModel.kt
package com.example.proyecto2.viewmodels

import androidx.lifecycle.ViewModel
import com.example.proyecto2.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Estado de la UI para las pantallas de cliente
data class ClientUiState(
    val favoritos: List<Producto> = emptyList() // Lista de productos que interesan al usuario
)

class ClientViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ClientUiState())
    val uiState = _uiState.asStateFlow()

    fun agregarAFavoritos(producto: Producto) {
        // Lógica para añadir un producto a la lista
        _uiState.update { currentState ->
            // Nos aseguramos de no añadir duplicados
            if (currentState.favoritos.any { it.id == producto.id }) {
                currentState // Si ya existe, no hacemos nada
            } else {
                currentState.copy(favoritos = currentState.favoritos + producto)
            }
        }
    }

    fun removerDeFavoritos(producto: Producto) {
        // Lógica para quitar un producto de la lista (útil para más adelante)
        _uiState.update { currentState ->
            currentState.copy(favoritos = currentState.favoritos.filter { it.id != producto.id })
        }
    }
}
