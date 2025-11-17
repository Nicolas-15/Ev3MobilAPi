// En: app/src/main/java/com/example/proyecto2/ui/viewmodels/LoginViewModel.kt
package com.example.proyecto2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto2.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// El UiState se mantiene igual, ya teníamos la variable `esAdmin`
data class LoginUiState(
    val email: String = "",
    val contrasena: String = "",
    val loginExitoso: Boolean = false,
    val esAdmin: Boolean = false,
    val mensajeError: String? = null
)

class LoginViewModel(private val userPrefsRepo: UserPreferencesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    // --- CREDENCIALES FIJAS DEL ADMINISTRADOR ---
    // Estas credenciales están "hardcodeadas". Cualquiera que use este email y contraseña será admin.
    private val ADMIN_EMAIL = "admin@admin.com"
    private val ADMIN_PASSWORD = "admin"

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, mensajeError = null) }
    }

    fun onContrasenaChange(contrasena: String) {
        _uiState.update { it.copy(contrasena = contrasena, mensajeError = null) }
    }

    fun onLoginClicked() {
        val emailIngresado = _uiState.value.email
        val contrasenaIngresada = _uiState.value.contrasena

        if (emailIngresado.isBlank() || contrasenaIngresada.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Correo y contraseña no pueden estar vacíos.") }
            return
        }

        // --- ¡NUEVA LÓGICA DE VERIFICACIÓN! ---
        viewModelScope.launch {

            // 1. PRIMERO, comprobamos si el usuario es el Administrador.
            if (emailIngresado == ADMIN_EMAIL && contrasenaIngresada == ADMIN_PASSWORD) {
                // Si las credenciales coinciden, actualizamos el estado para el login de admin.
                _uiState.update {
                    it.copy(
                        loginExitoso = true,
                        esAdmin = true // ¡Marcamos que es admin!
                    )
                }
                return@launch // Importante: Salimos de la corrutina para no seguir comprobando.
            }

            // 2. SI NO ES ADMIN, hacemos la comprobación normal para clientes.
            val emailGuardado = userPrefsRepo.userEmail.first()
            val contrasenaGuardada = userPrefsRepo.userPassword.first()

            // Comparamos los datos ingresados con los guardados para un usuario normal
            if (emailIngresado == emailGuardado && contrasenaIngresada == contrasenaGuardada) {
                // Si coinciden, el login es exitoso como cliente.
                _uiState.update {
                    it.copy(
                        loginExitoso = true,
                        esAdmin = false // Nos aseguramos de marcar que NO es admin.
                    )
                }
            } else {
                // Si no coincide ni con el admin ni con el usuario guardado, mostramos error.
                _uiState.update { it.copy(mensajeError = "Correo o contraseña incorrectos.") }
            }
        }
    }
}
