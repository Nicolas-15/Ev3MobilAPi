// En: app/src/main/java/com/example/proyecto2/ui/screens/auth/RegisterScreen.kt
package com.example.proyecto2.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto2.R
import com.example.proyecto2.viewmodels.RegisterViewModel
import com.example.proyecto2.viewmodels.RegisterViewModelFactory // <-- IMPORTANTE: Importa la nueva Factory
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBackToLogin: () -> Unit,
    // La creación del ViewModel ahora es más limpia y usa nuestra Factory
    registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(LocalContext.current.applicationContext)
    )
) {
    val uiState by registerViewModel.uiState.collectAsState()

    // Estado para controlar la visibilidad y disparar la animación de entrada
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100) // Pequeño delay para que la animación se vea fluida
        isVisible = true
    }

    // Navega hacia atrás cuando el registro es exitoso
    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 500)
            ) + fadeIn(animationSpec = tween(500))
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
        }
        //Aqui podemos agregar la imagen del login
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 500, delayMillis = 50)
            ) + fadeIn(animationSpec = tween(500, delayMillis = 50))
        ) {
            Image(
                painter = painterResource(id = R.drawable.loginimg),
                contentDescription = "Icono para registro",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            )
        }

        Spacer(Modifier.height(32.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 500, delayMillis = 150)
            ) + fadeIn(animationSpec = tween(500, delayMillis = 150))
        ) {
            OutlinedTextField(
                value = uiState.email,
                onValueChange = registerViewModel::onEmailChange,
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = uiState.mensajeError != null
            )
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 500, delayMillis = 250)
            ) + fadeIn(animationSpec = tween(500, delayMillis = 250))
        ) {
            OutlinedTextField(
                value = uiState.contrasena,
                onValueChange = registerViewModel::onContrasenaChange,
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = uiState.mensajeError != null
            )
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 500, delayMillis = 350)
            ) + fadeIn(animationSpec = tween(500, delayMillis = 350))
        ) {
            OutlinedTextField(
                value = uiState.confirmacionContrasena,
                onValueChange = registerViewModel::onConfirmacionContrasenaChange,
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = uiState.mensajeError != null
            )
        }

        // Animar la aparición del mensaje de error
        AnimatedVisibility(visible = uiState.mensajeError != null) {
            uiState.mensajeError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 500, delayMillis = 450)
            ) + fadeIn(animationSpec = tween(500, delayMillis = 450))
        ) {
            Button(
                onClick = { registerViewModel.onRegisterClicked() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarme")
            }
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 500, delayMillis = 550)
            ) + fadeIn(animationSpec = tween(500, delayMillis = 550))
        ) {
            TextButton(onClick = onNavigateBackToLogin) {
                Text("Ya tengo cuenta, iniciar sesión")
            }
        }
    }
}
