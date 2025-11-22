package com.example.proyecto2.ui.screens.screensapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.proyecto2.data.FakeProductDataSource
import com.example.proyecto2.data.model.Producto
import com.example.proyecto2.data.network.MyApiRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// 1. Definimos los estados posibles de la UI para la carga de datos
sealed interface ProductListUiState {
    data class Success(val products: List<Producto>) : ProductListUiState
    object Error : ProductListUiState
    object Loading : ProductListUiState
}

@Composable
fun ContenidoPrincipal(
    modifier: Modifier = Modifier,
    onProductClicked: (Producto) -> Unit
) {
    // 2. Creamos una variable de estado para manejar la UI
    var uiState by remember { mutableStateOf<ProductListUiState>(ProductListUiState.Loading) }

    // 3. LaunchedEffect ejecuta la llamada de red UNA SOLA VEZ cuando el Composable aparece
    LaunchedEffect(Unit) {
        uiState = try {
            // Cambiamos al hilo de IO para la llamada de red (buena práctica)
            val products = withContext(Dispatchers.IO) {
                // --- CORRECCIÓN AQUÍ: Se usa '.instance' en lugar de '.api' ---
                MyApiRetrofitClient.instance.getProducts()
            }
            ProductListUiState.Success(products)
        } catch (e: Exception) {
            // Si algo falla (servidor apagado, no hay internet), cambiamos al estado de Error
            // Imprimimos el error para poder depurar
            e.printStackTrace()
            ProductListUiState.Error
        }
    }

    // 4. Renderizamos la UI según el estado actual
    when (val currentState = uiState) {
        is ProductListUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProductListUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error al cargar los productos. \nAsegúrate de que el servidor esté encendido.")
            }
        }
        is ProductListUiState.Success -> {
            if (currentState.products.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos disponibles en este momento.")
                }
            } else {
                val productosPorCategoria = currentState.products.groupBy { it.categoria }
                LazyColumn(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    items(productosPorCategoria.entries.toList()) { (categoria: String, productos: List<Producto>) ->
                        CategoriaRow(
                            categoria = categoria,
                            productos = productos,
                            onProductClicked = onProductClicked
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriaRow(
    categoria: String,
    productos: List<Producto>,
    modifier: Modifier = Modifier,
    onProductClicked: (Producto) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = categoria,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Ver todo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productos) { producto ->
                ProductoCardModerno(
                    producto = producto,
                    modifier = Modifier.width(280.dp),
                    onProductClicked = onProductClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoCardModerno(
    producto: Producto,
    modifier: Modifier = Modifier,
    onProductClicked: (Producto) -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = { onProductClicked(producto) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = "Imagen de ${producto.nombre}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                ),
                                startY = 0.6f
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$${producto.precio.toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "4.8",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "Ver detalles →",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoCard(
    producto: Producto,
    modifier: Modifier = Modifier,
    onProductClicked: (Producto) -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = { onProductClicked(producto) }
    ) {
        Column {
            AsyncImage(
                model = producto.imagen,
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${producto.precio.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showSystemUi = true, name = "Contenido Principal (Preview con Datos Falsos)")
@Composable
fun PreviewContenidoPrincipalModerno() {
    // La preview ahora solo muestra el estado de éxito con datos falsos
    val productosPorCategoria = FakeProductDataSource.productos.groupBy { it.categoria }
    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(productosPorCategoria.entries.toList()) { (categoria: String, productos: List<Producto>) ->
            CategoriaRow(
                categoria = categoria,
                productos = productos,
                onProductClicked = {}
            )
        }
    }
}
