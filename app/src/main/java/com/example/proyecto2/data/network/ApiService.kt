package com.example.proyecto2.data.network

import com.example.proyecto2.data.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("/posts")
    suspend fun getPost(): List<Post>
}



