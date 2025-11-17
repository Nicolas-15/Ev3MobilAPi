// En: app/src/main/java/com/example/proyecto2/ui/viewmodels/RegisterViewModel.kt
package com.example.proyecto2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto2.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//Estado para la pantalla de registro
data class RegisterUiState(
    val email: String = "",
    val contrasena: String = "",
    val confirmacionContrasena: String = "",
    val registroExitoso: Boolean = false,
    val mensajeError: String? = null
)

// Modifica el constructor para aceptar el repositorio
class RegisterViewModel(private val userPrefsRepo: UserPreferencesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    // Limpia el error cuando el usuario empieza a escribir
    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email, mensajeError = null) } }
    fun onContrasenaChange(contrasena: String) { _uiState.update { it.copy(contrasena = contrasena, mensajeError = null) } }
    fun onConfirmacionContrasenaChange(confirmacionContrasena: String) { _uiState.update { it.copy(confirmacionContrasena = confirmacionContrasena, mensajeError = null) } }


    fun onRegisterClicked() {
        val state = uiState.value

        // Validaciones
        if (state.email.isBlank() || state.contrasena.isBlank() || state.confirmacionContrasena.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Todos los campos son obligatorios.") }
            return
        }
        if (state.contrasena != state.confirmacionContrasena) {
            _uiState.update { it.copy(mensajeError = "Las contraseñas no coinciden.") }
            return
        }

        // ¡Aquí está la magia! Guardamos el usuario usando el repositorio
        viewModelScope.launch {
            userPrefsRepo.saveUser(state.email, state.contrasena)
            // Una vez guardado, actualizamos la UI para navegar
            _uiState.update { it.copy(registroExitoso = true) }
        }
    }
}
