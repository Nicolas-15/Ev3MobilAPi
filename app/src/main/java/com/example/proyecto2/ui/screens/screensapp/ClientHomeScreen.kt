package com.example.proyecto2.ui.screens.screensapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto2.ui.theme.Proyecto2Theme

/**
 * Pantalla principal para el rol de Cliente.
 * Ofrece acceso a las funcionalidades clave del cliente.
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Cabecera de bienvenida ---
        Text(
            text = "¡Hola, bienvenido!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Explora nuestro catálogo y revisa tus pedidos.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Acciones Principales en Tarjetas ---
        ClientActionCard(
            title = "Ver Catálogo de Productos",
            description = "Descubre todo lo que tenemos para ofrecer.",
            icon = Icons.Default.Storefront,
            onClick = onNavigateToCatalog
        )

        ClientActionCard(
            title = "Mis Pedidos y Arriendos",
            description = "Consulta el historial y estado de tus solicitudes.",
            icon = Icons.AutoMirrored.Filled.ListAlt,
            onClick = onNavigateToMyOrders
        )

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de abajo al final

        // --- Botón para volver a la selección de rol ---
        OutlinedButton(
            onClick = onNavigateToRoleSelection,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cambiar de Rol")
        }
    }
}

/**
 * Tarjeta de acción reutilizable para la pantalla del cliente.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


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
