package com.example.proyecto2.ui.screens.screensapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto2.data.FakeProductDataSource
import com.example.proyecto2.data.model.Producto

//Realizamos cambios a la productCard debido a problemas en la muestra de los productos
// Ahora no tiene el onClick Vacio y se ejecuta la funcion pasando el mismo producto
// Categoria row e le añadio el parametrp onProductClicked que pasa a cada una de las productCard que crea */

@Composable
fun ContenidoPrincipal(
    modifier: Modifier = Modifier,
    onProductClicked: (Producto) -> Unit // <-- 1. AÑADIDO: Recibe la acción de clic
) {
    val productosPorCategoria = FakeProductDataSource.productos.groupBy { it.categoria }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ){
        items(productosPorCategoria.entries.toList()) { (categoria, productos) ->
            CategoriaRow(
                categoria = categoria,
                productos = productos,
                onProductClicked = onProductClicked // <-- 2. PASO: Se pasa la acción a CategoriaRow
            )
        }
    }
}
@Composable
fun CategoriaRow(
    categoria: String,
    productos: List<Producto>,
    modifier: Modifier = Modifier,
    onProductClicked: (Producto) -> Unit // <-- 1. AÑADIDO: Recibe la acción de clic
){
    Column(modifier = modifier) {
        Text(
            text = categoria,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productos) { producto ->
                ProductoCard(
                    producto = producto,
                    modifier = Modifier.width(250.dp),
                    onProductClicked = onProductClicked // <-- 2. PASO: Se pasa la acción a ProductoCard
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoCard(
    producto: Producto,
    modifier: Modifier = Modifier,
    onProductClicked: (Producto) -> Unit // <-- 1. AÑADIDO: Recibe la acción de clic
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = { onProductClicked(producto) } // <-- 3. USO: Al hacer clic, se ejecuta la acción con el producto actual
    ) {
        Column {
            Image(
                painter = painterResource(id = producto.imagenId),
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
@Preview(showSystemUi = true, name = "Contenido Principal")
@Composable
fun PreviewContenidoPrincipal() {
    // Para la preview, la acción de clic no hace nada.
    ContenidoPrincipal(onProductClicked = {})
}
