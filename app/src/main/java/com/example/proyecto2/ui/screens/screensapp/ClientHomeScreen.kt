package com.example.proyecto2.ui.screens.screensapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto2.ui.theme.Proyecto2Theme

/**
 * Pantalla principal para el usuario con rol de "Cliente".
 * Funciona como un menú central desde donde el cliente puede acceder a las
 * principales secciones de la aplicación, como el catálogo de productos y sus pedidos.
 *
 * @param onNavigateToCatalog Lambda que define la acción de navegación hacia la pantalla del catálogo.
 * @param onNavigateToMyOrders Lambda que define la acción de navegación hacia la pantalla de "Mis Pedidos".
 * @param onNavigateToRoleSelection Lambda que define la acción para "cerrar sesión" y volver a la pantalla de selección de rol.
 */
@Composable
fun ClientHomeScreen(
    onNavigateToCatalog: () -> Unit,
    onNavigateToMyOrders: () -> Unit,
    onNavigateToRoleSelection: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- Cabecera de bienvenida ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            // Icono decorativo que representa una tienda o catálogo.
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null, // Es decorativo, no necesita descripción.
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de bienvenida principal.
            Text(
                text = "¡Hola Cliente!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo o mensaje secundario.
            Text(
                text = "¿Qué te gustaría hacer hoy?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        // --- Tarjetas de Acción Principales ---
        // Cada tarjeta es un punto de entrada a una sección importante de la app.

        // Tarjeta para navegar al catálogo de productos.
        ImprovedActionCard(
            title = "Ver Catálogo",
            description = "Explora todos nuestros productos disponibles",
            icon = Icons.Default.Storefront,
            onClick = onNavigateToCatalog, // Acción de navegación.
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )

        // Tarjeta para navegar a la pantalla de "Mis Pedidos".
        ImprovedActionCard(
            title = "Mis Arriendos",
            description = "Revisa los productos que te interesan",
            icon = Icons.AutoMirrored.Filled.ListAlt,
            onClick = onNavigateToMyOrders, // Acción de navegación.
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )

        // Spacer con `weight` para empujar el botón de "volver" al final de la pantalla.
        Spacer(modifier = Modifier.weight(1f))

        // --- Botón para volver / Cerrar Sesión ---
        TextButton(
            onClick = onNavigateToRoleSelection, // Ejecuta la acción para volver a la pantalla de Login/Selección de Rol.
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Volver a selección de rol")
        }
    }
}

/**
 * Composable reutilizable para crear las tarjetas de acción con un diseño mejorado.
 * Es un buen ejemplo de cómo crear componentes de UI personalizados y reutilizables.
 *
 * @param title Título principal de la tarjeta.
 * @param description Texto descriptivo secundario.
 * @param icon Icono que se mostrará en la tarjeta.
 * @param onClick Acción que se ejecutará al pulsar la tarjeta.
 * @param containerColor Color de fondo de la tarjeta.
 * @param contentColor Color para el contenido (texto e iconos) de la tarjeta.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImprovedActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono principal a la izquierda.
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(contentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = contentColor
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            // Contenido de texto (título y descripción).
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = contentColor
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }

            // Icono de flecha a la derecha como indicador visual.
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = contentColor
                )
            }
        }
    }
}

/**
 * Función de Previsualización para `ClientHomeScreen`.
 * Permite ver el diseño de la pantalla en Android Studio sin ejecutar la aplicación.
 */
@Preview(showSystemUi = true, name = "Client Home Screen")
@Composable
fun PreviewClientHomeScreen() {
    Proyecto2Theme {
        ClientHomeScreen(
            onNavigateToCatalog = {},
            onNavigateToMyOrders = {},
            onNavigateToRoleSelection = {}
        )
    }
}
