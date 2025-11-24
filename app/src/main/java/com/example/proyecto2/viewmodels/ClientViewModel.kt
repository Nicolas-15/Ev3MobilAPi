package com.example.proyecto2.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto2.data.model.Producto
import com.example.proyecto2.data.network.MyApiRetrofitClient // Importamos el cliente para nuestro backend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

// --- PASO 1: Enriquecer el Estado de la UI ---
// Ahora el estado contendrá toda la información que necesita la pantalla del cliente.
data class ClientUiState(
    // La lista principal de productos que viene de la API
    val productos: List<Producto> = emptyList(),
    // Tu lista de favoritos, la conservamos
    val favoritos: List<Producto> = emptyList(),
    // Estados para dar feedback al usuario
    val isLoading: Boolean = false,
    val error: String? = null
)

class ClientViewModel : ViewModel() {
    // Usamos el nuevo ClientUiState que contiene todo
    private val _uiState = MutableStateFlow(ClientUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // En cuanto el ViewModel se inicie, llamamos a la API
        cargarProductosDesdeApi()
    }

    // --- PASO 2: Integrar la Lógica para Cargar Datos de la API ---
    fun cargarProductosDesdeApi() {
        // Usamos una corrutina para no bloquear la UI
        viewModelScope.launch {
            // Actualizamos el estado para mostrar que estamos cargando
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Llamamos a nuestro backend usando el cliente de Retrofit correcto
                val listaDeLaApi = MyApiRetrofitClient.instance.getProducts()

                // Si la llamada es exitosa, actualizamos el estado con la nueva lista
                _uiState.update { currentState ->
                    currentState.copy(
                        productos = listaDeLaApi,
                        isLoading = false // Dejamos de cargar
                    )
                }
            } catch (e: IOException) {
                // CORRECCIÓN: Usamos el parámetro 'e' para registrar el error en el Logcat.
                Log.e("ClientViewModel", "Error de red al cargar productos: ${e.message}")
                // Manejamos errores de red
                _uiState.update { currentState ->
                    currentState.copy(
                        error = "Error de conexión: No se pudo conectar al servidor.",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                // CORRECCIÓN: Usamos el parámetro 'e' para registrar el error en el Logcat.
                Log.e("ClientViewModel", "Error inesperado al cargar productos: ${e.message}")
                // Manejamos cualquier otro error
                _uiState.update { currentState ->
                    currentState.copy(
                        error = "Error inesperado. Por favor, inténtelo más tarde.",
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Busca un producto por su ID en la lista ya cargada en el estado.
     * Es más eficiente que una nueva llamada a la API para la pantalla de detalles.
     * La advertencia 'never used' desaparecerá cuando la uses en tu pantalla de detalles.
     */
    fun findProductoById(id: Int): Producto? {
        return _uiState.value.productos.find { it.id == id }
    }


    // --- PASO 3: Mantener tu Lógica de Favoritos (sin cambios) ---
    fun agregarAFavoritos(producto: Producto) {
        _uiState.update { currentState ->
            if (currentState.favoritos.any { it.id == producto.id }) {
                currentState
            } else {
                currentState.copy(favoritos = currentState.favoritos + producto)
            }
        }
    }

    fun removerDeFavoritos(producto: Producto) {
        _uiState.update { currentState ->
            currentState.copy(favoritos = currentState.favoritos.filter { it.id != producto.id })
        }
    }
}
