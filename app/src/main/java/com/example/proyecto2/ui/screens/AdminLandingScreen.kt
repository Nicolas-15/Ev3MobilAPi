package com.example.proyecto2.ui.screens

//Importaciones para iconos nuevos de las tarjetas
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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto2.ui.theme.Proyecto2Theme

/**
 * Pantalla principal del panel de administrador (Dashboard).
 * Ofrece un resumen del negocio y acceso a las funciones de gestión.
 */
@Composable
fun AdminLandingScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToOrders: () -> Unit,
    // Nueva rota para cambiar de rol, se mantiene solo para la navegacion de rutas
    onNavigateToRoleSelection: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Cabecera del Dashboard ---
        Text(
            text = "Panel de Administrador",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Gestiona el estado de tu negocio.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Indicadores Clave de Rendimiento (KPIs) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SimpleKpiCard("Ventas Hoy", "$1,250", modifier = Modifier.weight(1f))
            SimpleKpiCard("Pedidos Nuevos", "8", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Acciones de Gestión en Tarjetas ---
        AdminActionCard(
            title = "Gestionar Productos",
            description = "Añade, edita y elimina productos del catálogo.",
            icon = Icons.Default.AddBusiness,
            onClick = onNavigateToProducts
        )

        AdminActionCard(
            title = "Control de Inventario",
            description = "Actualiza el stock y la disponibilidad de los ítems.",
            icon = Icons.Default.Inventory,
            onClick = onNavigateToInventory
        )

        AdminActionCard(
            title = "Revisar Pedidos",
            description = "Consulta el historial de ventas y solicitudes.",
            icon = Icons.AutoMirrored.Filled.ReceiptLong,
            onClick = onNavigateToOrders
        )

        // Empuja el botón de "Cambiar Rol" hacia abajo.
        Spacer(modifier = Modifier.weight(1f))

        // Botón secundario para salir del panel de admin.
        OutlinedButton(
            onClick = onNavigateToRoleSelection,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cambiar de rol")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cambiar de Rol / Salir")
        }
    }
}

/**
 * Tarjeta reutilizable para las acciones principales del administrador.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminActionCard(
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
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Text(description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


// Composable para las tarjetas
@Composable
fun SimpleKpiCard(title: String, value: String, modifier: Modifier = Modifier) {
    OutlinedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(value, style = MaterialTheme.typography.titleLarge)
            Text(title, style = MaterialTheme.typography.labelMedium)
        }
    }
}


@Preview(showSystemUi = true, name = "Admin Landing Screen")
@Composable
fun PreviewAdminLandingScreen() {
    Proyecto2Theme {
        AdminLandingScreen(
            onNavigateToProducts = {},
            onNavigateToInventory = {},
            onNavigateToOrders = {},
            onNavigateToRoleSelection = {}
        )
    }
}
