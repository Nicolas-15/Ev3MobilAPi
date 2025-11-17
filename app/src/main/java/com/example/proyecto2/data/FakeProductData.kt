package com.example.proyecto2.data

import com.example.proyecto2.R
import com.example.proyecto2.data.model.Producto

// Esta clase nos dará una lista de productos para probar la UI.
object FakeProductDataSource {
    val productos = listOf(
        //Primera categoria de "Arriendos de Verano"
        Producto(
            id = 1,
            nombre = "Departamento en la Playa",
            descripcion = "Acogedor departamento con vista al mar, ideal para vacaciones. Incluye 2 dormitorios y 1 baño.",
            precio = 50000.0,
            imagenId = R.drawable.depto_playa,
            categoria = "Arriendo de Verano" //Categoria añadida
        ),
        Producto(
            id = 2,
            nombre = "Cabaña en el Bosque",
            descripcion = "Cabaña rústica para desconectarse de la ciudad. Perfecta para 4 personas.",
            precio = 75000.0,
            imagenId = R.drawable.cabana_bosque,
            categoria = "Arriendo de Verano" //Categoria añadida
        ),
        Producto(
            id = 3,
            nombre = "Casa de Campo",
            descripcion = "Casa comoda para disfrutar de un perfecto fin de semana. Ideal para vacaciones en familia.",
            precio = 35000.0,
            imagenId = R.drawable.casa_campo,
            categoria = "Arriendo de Verano" //Categoria añadida
        ),
        //Segunda Categoria para "Escapadas de fin de Semana"
        Producto(
            id = 4,
            nombre = "Casa en la Montaña",
            descripcion = "Casa en la montaña para disfrutar de un fin de semana lleno de aventuras. Ideal para familias.",
            precio = 45000.0,
            imagenId = R.drawable.casa_montana,
            categoria = "Escapadas de Fin de Semana" //Categoria añadida
        ),
        //Tercera Categoria propiedades en venta
        Producto(
            id = 5,
            nombre = "Departamento en el Centro",
            descripcion = "Departamento tranquilo en el centro de la ciudad. Ideal para familias.",
            precio = 40000.0,
            imagenId = R.drawable.depto_centro,
            categoria = "Propiedades en Venta" //Categoria añadida
        )
    )
}
