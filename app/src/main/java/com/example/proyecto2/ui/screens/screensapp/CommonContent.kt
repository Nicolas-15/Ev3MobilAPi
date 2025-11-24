package com.example.proyecto2.ui.screens.screensapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

/**
 * Define los diferentes estados posibles de la UI para la pantalla del catálogo.
 * Esto permite manejar de forma explícita los casos de Carga, Éxito y Error.
 */
sealed interface ProductListUiState {
    /** Estado de éxito: contiene la lista de productos cargada desde el backend. */
    data class Success(val products: List<Producto>) : ProductListUiState
    /** Estado de error: indica que ha ocurrido un problema al cargar los datos. */
    object Error : ProductListUiState
    /** Estado de carga: indica que la aplicación está esperando la respuesta del backend. */
    object Loading : ProductListUiState
}

/**
 * Composable principal que gestiona y muestra el catálogo de productos.
 * Se encarga de:
 * 1.  Realizar la llamada de red para obtener los productos.
 * 2.  Manejar los estados de la UI (Carga, Éxito, Error).
 * 3.  Mostrar la lista de productos agrupados por categoría.
 * 4.  Implementar la lógica de "Reintentar" en caso de error.
 *
 * @param modifier Modificador para personalizar el layout.
 * @param onProductClicked Lambda que se invoca cuando el usuario hace clic en un producto.
 */
@Composable
fun ContenidoPrincipal(
    modifier: Modifier = Modifier,
    onProductClicked: (Producto) -> Unit
) {
    // Variable de estado para controlar la UI (Carga, Éxito o Error).
    var uiState by remember { mutableStateOf<ProductListUiState>(ProductListUiState.Loading) }
    // Clave que, al cambiar, vuelve a disparar el `LaunchedEffect` para reintentar la carga.
    var retryKey by remember { mutableStateOf(0) }

    // `LaunchedEffect` se ejecuta cuando el Composable entra en la composición y cada vez que `retryKey` cambia.
    // Es el lugar ideal para realizar llamadas de red de forma segura.
    LaunchedEffect(retryKey) {
        // Al iniciar la carga (o reintentar), mostramos el indicador de progreso.
        uiState = ProductListUiState.Loading
        uiState = try {
            // Se usa `withContext(Dispatchers.IO)` para ejecutar la llamada de red en un hilo
            // secundario, evitando bloquear la UI. Es una buena práctica de concurrencia.
            val products = withContext(Dispatchers.IO) {
                MyApiRetrofitClient.instance.getProducts()
            }
            // Si la llamada es exitosa, actualizamos el estado con la lista de productos.
            ProductListUiState.Success(products)
        } catch (e: Exception) {
            // Si la llamada falla (p.ej., no hay internet o el backend está caído),
            // cambiamos al estado de Error.
            e.printStackTrace() // Imprime el error en el Logcat para depuración.
            ProductListUiState.Error
        }
    }

    // Renderiza la UI correspondiente al estado actual.
    when (val currentState = uiState) {
        is ProductListUiState.Loading -> {
            // Muestra un indicador de carga circular en el centro de la pantalla.
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProductListUiState.Error -> {
            // Muestra un mensaje de error y un botón para que el usuario pueda reintentar la carga.
            // Esto mejora mucho la experiencia de usuario ante fallos de conexión.
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("Error al cargar los productos.\nAsegúrate de que el servidor esté encendido.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { retryKey++ }) { // Al hacer clic, se incrementa `retryKey` para disparar el `LaunchedEffect` de nuevo.
                        Text("Reintentar")
                    }
                }
            }
        }
        is ProductListUiState.Success -> {
            // Si la carga fue exitosa, pero la lista está vacía, mostramos un mensaje informativo.
            if (currentState.products.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos disponibles en este momento.")
                }
            } else {
                // Agrupa la lista de productos por su campo 'categoria'.
                val productosPorCategoria = currentState.products.groupBy { it.categoria }
                // Muestra las categorías y sus productos en una lista vertical.
                LazyColumn(
                    modifier = modifier.background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    items(productosPorCategoria.entries.toList()) { (categoria, productos) ->
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

/**
 * Muestra una fila de una categoría, incluyendo su título y una lista horizontal
 * de tarjetas de productos (`ProductoCardModerno`).
 *
 * @param categoria El nombre de la categoría a mostrar.
 * @param productos La lista de productos pertenecientes a esa categoría.
 * @param modifier Modificador para personalizar el layout.
 * @param onProductClicked Lambda que se pasa a cada tarjeta de producto.
 */
@Composable
fun CategoriaRow(
    categoria: String,
    productos: List<Producto>,
    modifier: Modifier = Modifier,
    onProductClicked: (Producto) -> Unit
) {
    Column(modifier = modifier) {
        // Título de la categoría.
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
        }

        // Lista horizontal de productos para esta categoría.
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

/**
 * Tarjeta con diseño moderno para mostrar la información de un solo producto.
 * Muestra la imagen, precio, nombre y descripción del producto.
 * Toda la tarjeta es clicable para navegar a la pantalla de detalle.
 *
 * @param producto El objeto `Producto` a mostrar.
 * @param modifier Modificador para personalizar el layout.
 * @param onProductClicked Lambda que se invoca al hacer clic en la tarjeta.
 */
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
        onClick = { onProductClicked(producto) } // Toda la tarjeta es clicable.
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // Carga la imagen del producto desde una URL usando Coil.
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = "Imagen de ${producto.nombre}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                // Degradado oscuro en la parte inferior de la imagen para asegurar que el precio sea legible.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                                startY = 0.6f
                            )
                        )
                )
                // Etiqueta con el precio del producto en la esquina superior derecha.
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
            // Sección inferior de la tarjeta con el nombre y la descripción.
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Añade "..." si el texto es muy largo.
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis // Añade "..." si la descripción es muy larga.
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * --- ATENCIÓN: COMPOSABLE ANTIGUO / NO UTILIZADO ---
 * Esta es una versión más simple de la tarjeta de producto.
 * Se mantiene aquí como referencia, pero no se está usando en el catálogo principal.
 * Se podría eliminar para limpiar el código.
 */
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

/**
 * Función de Previsualización para el `ContenidoPrincipal`.
 * Muestra el catálogo usando una lista de productos falsos (`FakeProductDataSource`),
 * lo que permite ver el diseño en Android Studio sin necesidad de ejecutar la app ni el backend.
 */
@Preview(showSystemUi = true, name = "Contenido Principal (Con Datos Falsos)")
@Composable
fun PreviewContenidoPrincipalModerno() {
    // Usamos datos falsos para la previsualización.
    val productosPorCategoria = FakeProductDataSource.productos.groupBy { it.categoria }
    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(productosPorCategoria.entries.toList()) { (categoria, productos) ->
            CategoriaRow(
                categoria = categoria,
                productos = productos,
                onProductClicked = {}
            )
        }
    }
}
