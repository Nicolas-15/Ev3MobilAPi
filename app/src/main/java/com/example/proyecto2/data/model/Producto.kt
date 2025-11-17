package com.example.proyecto2.data.model

import androidx.annotation.DrawableRes

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    @DrawableRes val imagenId: Int // Usamos el ID del recurso de la imagen
)
