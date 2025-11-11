package com.example.proyecto2.network

import com.example.proyecto2.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}
