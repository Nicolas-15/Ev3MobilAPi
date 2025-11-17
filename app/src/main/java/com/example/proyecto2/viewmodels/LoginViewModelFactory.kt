// En: app/src/main/java/com/example/proyecto2/ui/viewmodels/LoginViewModelFactory.kt
package com.example.proyecto2.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto2.repository.UserPreferencesRepository

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val userPrefsRepo = UserPreferencesRepository(context.applicationContext)
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userPrefsRepo) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}
