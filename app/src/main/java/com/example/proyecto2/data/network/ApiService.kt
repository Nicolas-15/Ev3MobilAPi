package com.example.proyecto2.data.network

import com.example.proyecto2.data.model.Post
import com.example.proyecto2.data.model.Producto
import retrofit2.http.GET

interface ApiService {
    @GET("/posts")
    suspend fun getPosts(): List<Post>

    @GET("productos")
    suspend fun getProducts(): List<Producto>
}
