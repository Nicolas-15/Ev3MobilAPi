package com.example.proyecto2

import com.example.proyecto2.data.model.Post
import com.example.proyecto2.viewmodels.PostViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest : StringSpec({
    "postList debe contener los datos esperados despues de fetchPosts()"{
        //Crear una subclase falsa de PostViewModel que sobreescribe el repo
        val takePosts = listOf(
            Post(1, 1, "Título 1", "Cuerpo 1"),
            Post(2, 2, "Título 2", "Cuerpo 2")
        )

        val testViewModel = object : PostViewModel() {
            override fun fetchPosts() {
                _postList.value = takePosts
            }
        }

        runTest {
            testViewModel.fetchPosts()
            testViewModel.postList.value shouldContainExactly takePosts
        }
    }
})
