package com.example.proyecto2.data.model

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagen: String // URL de la imagen desde el backend
)
