package com.example.proyecto2.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto2.model.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true

            // 🔹 Simula una carga de datos (API, base de datos, etc.)
            delay(1500)

            // 🔹 Datos de ejemplo
            _posts.value = listOf(
                Post(1, "Primer Post", "Este es el contenido del primer post."),
                Post(2, "Segundo Post", "Aquí va el contenido del segundo post."),
                Post(3, "Tercer Post", "Un texto de prueba para mostrar en la app.")
            )

            _isLoading.value = false
        }
    }
}
