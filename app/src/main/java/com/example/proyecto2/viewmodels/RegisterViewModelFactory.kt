// En: app/src/main/java/com/example/proyecto2/ui/viewmodels/RegisterViewModelFactory.kt
package com.example.proyecto2.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto2.repository.UserPreferencesRepository

class RegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            // Aquí creamos el repositorio, fuera de cualquier función Composable
            val userPrefsRepo = UserPreferencesRepository(context.applicationContext)

            // Suprimimos una advertencia de casting que es segura en este contexto
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(userPrefsRepo) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}
