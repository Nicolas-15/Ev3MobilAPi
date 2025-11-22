package com.example.proyecto2.data.network

import com.example.proyecto2.data.model.Post
import com.example.proyecto2.data.model.Producto
import retrofit2.http.GET

interface ApiService {
    @GET("/posts")
    suspend fun getPost(): List<Post>

    // Apuntamos al endpoint del backend que devuelve la lista de productos
    @GET("productos")
    suspend fun getProducts(): List<Producto>
}
