package com.example.proyecto2.repository
import com.example.proyecto2.data.model.Post
import com.example.proyecto2.data.network.RetrofitInstance


open class PostRepository {
    open suspend fun getPosts(): List<Post>{
        return RetrofitInstance.api.getPosts()
    }
}