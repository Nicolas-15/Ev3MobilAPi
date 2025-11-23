package com.example.proyecto2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto2.data.model.Post
import com.example.proyecto2.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Modifica la clase para que acepte el repositorio en el constructor
// Dale un valor por defecto para que no rompa tu código actual.
open class PostViewModel(
    private val repository: PostRepository = PostRepository()
) : ViewModel() {

    // Ya no necesitas crearlo aquí
    // private val repository = PostRepository()

    protected val _postList = MutableStateFlow<List<Post>>(emptyList())
    open val postList: StateFlow<List<Post>> = _postList

    init {
        fetchPosts()
    }

    open fun fetchPosts() {
        viewModelScope.launch {
            try {
                _postList.value = repository.getPosts()
            } catch (e: Exception) {
                // Manejo de errores
                println("Error al obtener datos: ${e.localizedMessage}")
            }
        }
    }
}
