// En: app/src/main/java/com/example/proyecto2/data/datastore/UserPreferencesRepository.kt
package com.example.proyecto2.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear la instancia de DataStore a nivel de la app
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    // 1. Definimos las claves para guardar los datos
    private object PreferencesKeys {
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PASSWORD = stringPreferencesKey("user_password") // ¡OJO! En una app real, la contraseña debe estar hasheada.
    }

    // 2. Función para GUARDAR los datos del usuario
    suspend fun saveUser(email: String, contrasena: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_EMAIL] = email
            preferences[PreferencesKeys.USER_PASSWORD] = contrasena
        }
    }

    // 3. Flow para LEER el email del usuario
    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_EMAIL]
    }

    // 4. Flow para LEER la contraseña del usuario
    val userPassword: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_PASSWORD]
    }
}
