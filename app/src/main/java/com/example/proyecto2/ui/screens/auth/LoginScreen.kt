// En: app/src/main/java/com/example/proyecto2/ui/screens/auth/LoginScreen.kt
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
import com.example.proyecto2.viewmodels.LoginViewModel
import com.example.proyecto2.viewmodels.LoginViewModelFactory // <-- IMPORTANTE: Importa la nueva Factory
import kotlinx.coroutines.delay
import com.example.proyecto2.R

@Composable
fun LoginScreen(
    onLoginSuccessAsAdmin: () -> Unit,
    onLoginSuccessAsClient: () -> Unit,
    onNavigateToRegister: () -> Unit,
    // Actualizamos la creación del ViewModel para usar nuestra Factory
    loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(LocalContext.current.applicationContext)
    )
) {
    val uiState by loginViewModel.uiState.collectAsState()

    // Estado para controlar la visibilidad y disparar la animación de entrada
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        // Pequeño delay para asegurar que la pantalla está lista para animar
        delay(100)
        isVisible = true
    }

    // Efecto para navegar cuando el login es exitoso
    LaunchedEffect(uiState.loginExitoso) {
        if (uiState.loginExitoso) {
            if (uiState.esAdmin) {
                onLoginSuccessAsAdmin()
            } else {
                onLoginSuccessAsClient()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Cada grupo de elementos se envuelve en AnimatedVisibility
        // para crear un efecto de entrada escalonado (staggered).

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 500)
            ) + fadeIn(animationSpec = tween(500))
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
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
                painter = painterResource(id = R.drawable.iniciar_sesion),
                contentDescription = "Icono para Login",
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
                onValueChange = loginViewModel::onEmailChange,
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
                onValueChange = loginViewModel::onContrasenaChange,
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                animationSpec = tween(durationMillis = 500, delayMillis = 350)
            ) + fadeIn(animationSpec = tween(500, delayMillis = 350))
        ) {
            Button(
                onClick = { loginViewModel.onLoginClicked() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 500, delayMillis = 450)
            ) + fadeIn(animationSpec = tween(500, delayMillis = 450))
        ) {
            TextButton(onClick = onNavigateToRegister) {
                Text("No tengo cuenta, registrarme")
            }
        }
    }
}
